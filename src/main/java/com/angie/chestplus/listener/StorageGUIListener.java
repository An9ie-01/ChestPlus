package com.angie.chestplus.listener;

import com.angie.chestplus.ChestPlus;
import com.angie.chestplus.config.ConfigManager;
import com.angie.chestplus.config.MessageManager;
import com.angie.chestplus.config.GuiConfigManager;
import com.angie.chestplus.gui.StorageGUI;
import com.angie.chestplus.model.ChestConfig;
import com.angie.chestplus.model.StorageHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Handles storage GUI interaction events.
 */
public class StorageGUIListener implements Listener {

    private final ChestPlus plugin;

    public StorageGUIListener(ChestPlus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();

        if (!(holder instanceof StorageHolder storageHolder)) return;

        String guiKey = storageHolder.getGuiKey();
        if (!guiKey.equalsIgnoreCase("MAIN")) return;

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) return;

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType().isAir()) return;

        GuiConfigManager guiConfig = plugin.getGuiConfigManager();
        int slot = event.getSlot();
        String iconKey = guiConfig.getSlotIconMap().get(slot);
        if (iconKey == null || !iconKey.startsWith("chest-")) return;

        int chestId;
        try {
            chestId = Integer.parseInt(iconKey.replace("chest-", ""));
        } catch (NumberFormatException e) {
            return;
        }

        ConfigManager configManager = plugin.getConfigManager();
        ChestConfig chestConfig = configManager.getChestConfig(chestId);
        MessageManager messageManager = plugin.getMessageManager();

        if (chestConfig == null) {
            player.sendMessage(messageManager.get("invalid-storage"));
            return;
        }

        if (!player.hasPermission(chestConfig.getPermission())) {
            player.sendMessage(messageManager.get("no-permission"));
            return;
        }

        boolean opened = new StorageGUI(plugin).open(player, chestId, player.getUniqueId());
        if (opened) {
            player.sendMessage(plugin.getMessageManager().get("storage-opened")
                    .replace("%id%", String.valueOf(chestId)));
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        if (!(inv.getHolder() instanceof StorageHolder holder)) return;
        if (!holder.getGuiKey().startsWith("CHEST_")) return;

        UUID owner = holder.getOwnerUUID();
        int chestId = holder.getChestId();

        plugin.getStorageLockManager().unlock(owner, chestId);

        ItemStack[] now = inv.getContents().clone();
        String key = owner + ":" + chestId;

        ItemStack[] last = plugin.getStorageDataManager().getLastSavedCache(key);
        if (!plugin.getStorageDataManager().hasChanged(last, now)) return;

        plugin.getStorageDataManager().updateCache(key, now);
        plugin.getStorageDataManager().saveAsync(owner, chestId, now);
    }
}
