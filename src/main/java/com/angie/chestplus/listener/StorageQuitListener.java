package com.angie.chestplus.listener;

import com.angie.chestplus.ChestPlus;
import com.angie.chestplus.model.StorageHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

/**
 * Saves open chest on player quit.
 */
public class StorageQuitListener implements Listener {

    private final ChestPlus plugin;

    public StorageQuitListener(ChestPlus plugin) {
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
