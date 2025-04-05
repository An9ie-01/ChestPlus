package com.angie.chestplus.model;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;

/**
 * Represents a custom holder for player chests.
 */
public class StorageHolder implements InventoryHolder {

    private final String guiKey;
    private final UUID ownerUUID;
    private final int chestId;

    public StorageHolder(String guiKey, UUID ownerUUID, int chestId) {
        this.guiKey = guiKey;
        this.ownerUUID = ownerUUID;
        this.chestId = chestId;
    }

    public String getGuiKey() {
        return guiKey;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public int getChestId() {
        return chestId;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
