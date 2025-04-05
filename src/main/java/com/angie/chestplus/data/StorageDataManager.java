package com.angie.chestplus.data;

import com.angie.chestplus.ChestPlus;
import com.angie.chestplus.util.FileUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

/**
 * Handles saving/loading of storage contents and caching.
 */
public class StorageDataManager {

    private final ChestPlus plugin;
    private final File folder;
    private final Map<String, ItemStack[]> lastSavedCache = new HashMap<>();

    public StorageDataManager(ChestPlus plugin) {
        this.plugin = plugin;
        this.folder = new File(plugin.getDataFolder(), "storage");
        if (!folder.exists()) folder.mkdirs();
    }

    private File getFile(UUID uuid) {
        return new File(folder, uuid.toString() + ".yml");
    }

    public ItemStack[] load(UUID uuid, int chestId) {
        File file = getFile(uuid);
        if (!file.exists()) return null;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<ItemStack> list = (List<ItemStack>) config.getList("storage." + chestId + ".contents");
        if (list == null) return null;

        ItemStack[] contents = list.toArray(new ItemStack[0]);
        lastSavedCache.put(uuid + ":" + chestId, cloneContents(contents)); // update cache
        return contents;
    }

    public void saveSync(UUID uuid, int chestId, ItemStack[] contents) {
        File file = getFile(uuid);
        YamlConfiguration config = file.exists()
                ? YamlConfiguration.loadConfiguration(file)
                : new YamlConfiguration();

        config.set("storage." + chestId + ".contents", contents);
        FileUtil.saveNow(config, file); // synchronous save
    }

    public void saveAsync(UUID uuid, int chestId, ItemStack[] contents) {
        File file = getFile(uuid);
        YamlConfiguration config = file.exists()
                ? YamlConfiguration.loadConfiguration(file)
                : new YamlConfiguration();

        config.set("storage." + chestId + ".contents", contents);
        FileUtil.saveAsync(plugin, config, file); // async save
    }

    public boolean hasChanged(ItemStack[] before, ItemStack[] after) {
        if (before == null || after == null) return true;
        if (before.length != after.length) return true;

        for (int i = 0; i < before.length; i++) {
            ItemStack a = before[i];
            ItemStack b = after[i];
            if (!Objects.equals(a, b)) return true;
        }
        return false;
    }

    public ItemStack[] getLastSavedCache(String key) {
        return lastSavedCache.get(key);
    }

    public void updateCache(String key, ItemStack[] contents) {
        lastSavedCache.put(key, cloneContents(contents));
    }

    private ItemStack[] cloneContents(ItemStack[] original) {
        ItemStack[] clone = new ItemStack[original.length];
        for (int i = 0; i < original.length; i++) {
            if (original[i] != null) clone[i] = original[i].clone();
        }
        return clone;
    }

    public void clearCache() {
        lastSavedCache.clear();
    }
}
