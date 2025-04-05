package com.angie.chestplus.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages access locks to prevent multiple players from editing the same chest.
 */
public class StorageLockManager {

    private final Map<String, UUID> lockedChests = new ConcurrentHashMap<>();

    private String toKey(UUID ownerUUID, int chestId) {
        return ownerUUID.toString() + ":" + chestId;
    }

    public boolean isLocked(UUID ownerUUID, int chestId) {
        return lockedChests.containsKey(toKey(ownerUUID, chestId));
    }

    public boolean isLockedByOther(UUID ownerUUID, int chestId, UUID accessorUUID) {
        UUID current = lockedChests.get(toKey(ownerUUID, chestId));
        return current != null && !current.equals(accessorUUID);
    }

    public boolean lock(UUID ownerUUID, int chestId, UUID accessorUUID) {
        return lockedChests.putIfAbsent(toKey(ownerUUID, chestId), accessorUUID) == null;
    }

    public void unlock(UUID ownerUUID, int chestId) {
        lockedChests.remove(toKey(ownerUUID, chestId));
    }

    public void lockWithTimeout(UUID ownerUUID, int chestId, UUID accessorUUID, long ticks, Plugin plugin) {
        String key = toKey(ownerUUID, chestId);
        lockedChests.putIfAbsent(key, accessorUUID);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            UUID current = lockedChests.get(key);
            if (current != null && current.equals(accessorUUID)) {
                lockedChests.remove(key);
            }
        }, ticks);
    }
}
