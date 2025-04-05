package com.angie.customChest.command;

import com.angie.customChest.CustomChest;
import com.angie.customChest.config.GuiConfigManager;
import com.angie.customChest.config.MessageManager;
import com.angie.customChest.config.SettingsManager;
import com.angie.customChest.gui.MainStorageGUI;
import com.angie.customChest.gui.StorageGUI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StorageCommand implements CommandExecutor {

    private final CustomChest plugin;

    public StorageCommand(CustomChest plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("콘솔에서는 사용할 수 없습니다.");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("리로드")) {
            if (!player.hasPermission("customchest.reload")) {
                player.sendMessage(plugin.getMessageManager().get("no-permission"));
                return true;
            }

            plugin.getConfigManager().loadConfig();
            plugin.setGuiConfigManager(new GuiConfigManager(plugin));
            plugin.setMessageManager(new MessageManager(plugin));
            plugin.setSettingsManager(new SettingsManager(plugin));

            player.sendMessage(plugin.getMessageManager().get("reloaded"));
            return true;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("열기")) {
            if (!player.hasPermission("customchest.open.other")) {
                player.sendMessage(plugin.getMessageManager().get("no-permission"));
                return true;
            }

            String targetName = args[1];
            int chestId;

            try {
                chestId = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                player.sendMessage("§c숫자로 된 창고 번호를 입력해주세요.");
                return true;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
            if (target == null || !target.hasPlayedBefore()) {
                player.sendMessage("§c해당 플레이어를 찾을 수 없습니다.");
                return true;
            }

            plugin.getLogger().info(player.getName() + " 가 " + targetName + "의 창고 #" + chestId + " 열람 시도");

            boolean opened = new StorageGUI(plugin).open(player, chestId, target.getUniqueId());
            if (opened) {
                String message;
                if (target.equals(player)) {
                    message = plugin.getMessageManager().get("storage-opened")
                            .replace("%id%", String.valueOf(chestId));
                } else {
                    message = plugin.getMessageManager().get("storage-opened-other")
                            .replace("%id%", String.valueOf(chestId))
                            .replace("%target%", targetName);
                }
                player.sendMessage(message);
            }
            return true;
        }

        new MainStorageGUI(plugin).open(player);
        return true;
    }
}
