package com.angie.chestplus.command;

import com.angie.chestplus.ChestPlus;
import com.angie.chestplus.config.GuiConfigManager;
import com.angie.chestplus.config.MessageManager;
import com.angie.chestplus.config.SettingsManager;
import com.angie.chestplus.gui.MainStorageGUI;
import com.angie.chestplus.gui.StorageGUI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StorageCommand implements CommandExecutor {

    private final ChestPlus plugin;

    public StorageCommand(ChestPlus plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used in-game.");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("chestplus.reload")) {
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

        if (args.length == 3 && args[0].equalsIgnoreCase("open")) {
            if (!player.hasPermission("chestplus.open.other")) {
                player.sendMessage(plugin.getMessageManager().get("no-permission"));
                return true;
            }

            String targetName = args[1];
            int chestId;

            try {
                chestId = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                player.sendMessage("§cPlease enter a valid chest number.");
                return true;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
            if (target == null || !target.hasPlayedBefore()) {
                player.sendMessage("§cPlayer not found.");
                return true;
            }

            plugin.getLogger().info(player.getName() + " attempted to open chest #" + chestId + " of " + targetName);

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
