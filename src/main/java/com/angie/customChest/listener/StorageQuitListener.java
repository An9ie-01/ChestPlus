package com.angie.customChest.listener;

import com.angie.customChest.CustomChest;
import com.angie.customChest.model.StorageHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class StorageQuitListener implements Listener {

    private final CustomChest plugin;

    public StorageQuitListener(CustomChest plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Inventory topInv = player.getOpenInventory().getTopInventory();

        if (!(topInv.getHolder() instanceof StorageHolder holder)) return;
        if (!holder.getGuiKey().startsWith("CHEST_")) return;

        UUID owner = holder.getOwnerUUID();
        int chestId = holder.getChestId();

        plugin.getStorageDataManager().saveAsync(owner, chestId, topInv.getContents());
    }
}
