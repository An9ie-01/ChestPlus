package com.angie.customChest.config;

import com.angie.customChest.CustomChest;
import com.angie.customChest.model.ChestConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private final CustomChest plugin;
    private final Map<Integer, ChestConfig> chestConfigs = new HashMap<>();
    private int maxSlots;

    public ConfigManager(CustomChest plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.reloadConfig();
        chestConfigs.clear();

        maxSlots = plugin.getConfig().getInt("storage.max-slots", 5);

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("storage.chests");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                try {
                    int id = Integer.parseInt(key);
                    String permission = section.getString(key + ".permission", "customchest.open." + id);
                    int size = section.getInt(key + ".size", 45);

                    ChestConfig config = new ChestConfig(id, permission, size);
                    chestConfigs.put(id, config);
                } catch (NumberFormatException e) {
                    plugin.getLogger().warning("config.yml > storage.chests: 잘못된 창고 ID 형식: " + key);
                }
            }
        }
    }

    public int getMaxSlots() {
        return maxSlots;
    }

    public ChestConfig getChestConfig(int id) {
        return chestConfigs.get(id);
    }

    public Map<Integer, ChestConfig> getAllChestConfigs() {
        return chestConfigs;
    }
}
