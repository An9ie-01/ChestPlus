package com.angie.customChest.data;

import com.angie.customChest.CustomChest;
import com.angie.customChest.util.FileUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class StorageDataManager {

    private final CustomChest plugin;
    private final File folder;
    private final Map<String, ItemStack[]> lastSavedCache = new HashMap<>();


    public StorageDataManager(CustomChest plugin) {
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
        lastSavedCache.put(uuid + ":" + chestId, cloneContents(contents)); // ✅ 캐시 저장
        return contents;
    }


    public void saveSync(UUID uuid, int chestId, ItemStack[] contents) {
        String path = "storage." + chestId + ".contents";
        File file = getFile(uuid);

        YamlConfiguration config = file.exists()
                ? YamlConfiguration.loadConfiguration(file)
                : new YamlConfiguration();

        config.set(path, contents);
        FileUtil.saveNow(config, file); // 동기 저장
    }
    public void saveAsync(UUID uuid, int chestId, ItemStack[] contents) {
        String path = "storage." + chestId + ".contents";
        File file = getFile(uuid);

        YamlConfiguration config = file.exists()
                ? YamlConfiguration.loadConfiguration(file)
                : new YamlConfiguration();

        config.set(path, contents);

        FileUtil.saveAsync(plugin, config, file);
    }
    public boolean hasChanged(ItemStack[] before, ItemStack[] after) {
        if (before == null || after == null) return true;
        if (before.length != after.length) return true;

        for (int i = 0; i < before.length; i++) {
            ItemStack a = before[i] == null ? null : before[i];
            ItemStack b = after[i] == null ? null : after[i];

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
            if (original[i] != null)
                clone[i] = original[i].clone();
        }
        return clone;
    }
    public void clearCache() {
        lastSavedCache.clear();
    }
}
