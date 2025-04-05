package com.angie.chestplus.config;

import com.angie.chestplus.ChestPlus;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Loads plugin settings from settings.yml.
 */
public class SettingsManager {

    private final ChestPlus plugin;
    private final YamlConfiguration config;

    public SettingsManager(ChestPlus plugin) {
        this.plugin = plugin;

        File file = new File(plugin.getDataFolder(), "settings.yml");
        if (!file.exists()) {
            plugin.saveResource("settings.yml", false);
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public boolean isAutoSaveEnabled() {
        return config.getBoolean("auto-save.enabled", true);
    }

    public long getAutoSaveIntervalTicks() {
        return config.getLong("auto-save.interval-ticks", 6000L);
    }

    public boolean isAutoDeleteEnabled() {
        return config.getBoolean("auto-delete.enabled", true);
    }

    public long getAutoDeleteKeepSeconds() {
        return config.getLong("auto-delete.keep-seconds", 86400L);
    }

    public String getAutoDeleteFolderName() {
        return config.getString("auto-delete.folder", "autosave");
    }
}
