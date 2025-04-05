package com.angie.customChest.gui;

import com.angie.customChest.CustomChest;
import com.angie.customChest.model.ChestConfig;
import com.angie.customChest.model.StorageHolder;
import com.angie.customChest.util.StorageLockManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class StorageGUI {

    private final CustomChest plugin;

    public StorageGUI(CustomChest plugin) {
        this.plugin = plugin;
    }

    public boolean open(Player viewer, int chestId, UUID ownerUUID) {
        StorageLockManager lockManager = plugin.getStorageLockManager();
        if (lockManager.isLockedByOther(ownerUUID, chestId, viewer.getUniqueId())) {
            viewer.sendMessage(plugin.getMessageManager().get("storage-in-use"));
            return false;
        }

        lockManager.lockWithTimeout(ownerUUID, chestId, viewer.getUniqueId(), 20L * 60 * 5, plugin); // 10초로 설정


        ChestConfig chestConfig = plugin.getConfigManager().getChestConfig(chestId);
        if (chestConfig == null) return false;

        int size = chestConfig.getSize();

        String name = Bukkit.getOfflinePlayer(ownerUUID).getName();
        if (name == null) name = "Unknown";

        String title = "창고 #" + chestId + " (" + name + ")";
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
