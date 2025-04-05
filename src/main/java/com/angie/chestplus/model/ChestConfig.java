package com.angie.chestplus.model;

/**
 * Represents a single chest configuration entry.
 */
public class ChestConfig {

    private final int id;
    private final String permission;
    private final int size;

    public ChestConfig(int id, String permission, int size) {
        this.id = id;
        this.permission = permission;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public String getPermission() {
        return permission;
    }

    public int getSize() {
        return size;
    }
}
