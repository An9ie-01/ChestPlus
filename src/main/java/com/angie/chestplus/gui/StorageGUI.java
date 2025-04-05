package com.angie.chestplus.gui;

import com.angie.chestplus.ChestPlus;
import com.angie.chestplus.model.ChestConfig;
import com.angie.chestplus.model.StorageHolder;
import com.angie.chestplus.util.StorageLockManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Opens and manages an individual storage chest GUI.
 */
public class StorageGUI {

    private final ChestPlus plugin;

    public StorageGUI(ChestPlus plugin) {
        this.plugin = plugin;
    }

    public boolean open(Player viewer, int chestId, UUID ownerUUID) {
        StorageLockManager lockManager = plugin.getStorageLockManager();

        // If already locked by someone else, deny access
        if (lockManager.isLockedByOther(ownerUUID, chestId, viewer.getUniqueId())) {
            viewer.sendMessage(plugin.getMessageManager().get("storage-in-use"));
            return false;
        }

        // Lock the chest (with 5-minute timeout)
        lockManager.lockWithTimeout(ownerUUID, chestId, viewer.getUniqueId(), 20L * 60 * 5, plugin);

        ChestConfig chestConfig = plugin.getConfigManager().getChestConfig(chestId);
        if (chestConfig == null) return false;

        int size = chestConfig.getSize();

        String name = Bukkit.getOfflinePlayer(ownerUUID).getName();
        if (name == null) name = "Unknown";

        String title = "Storage #" + chestId + " (" + name + ")";
        Inventory gui = Bukkit.createInventory(
                new StorageHolder("CHEST_" + chestId, ownerUUID, chestId),
                size,
                title
        );

        ItemStack[] contents = plugin.getStorageDataManager().load(ownerUUID, chestId);
        if (contents != null) gui.setContents(contents);

        viewer.openInventory(gui);
        return true;
    }

    public void open(Player player, int chestId) {
        open(player, chestId, player.getUniqueId());
    }
}
