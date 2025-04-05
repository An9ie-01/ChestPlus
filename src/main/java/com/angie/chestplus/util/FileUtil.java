package com.angie.chestplus.util;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    /**
     * Save a YAML file asynchronously (non-blocking).
     */
    public static void saveAsync(Plugin plugin, YamlConfiguration config, File file) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                config.save(file);
            } catch (IOException e) {
                plugin.getLogger().warning("Failed to save file: " + file.getName());
                e.printStackTrace();
            }
        });
    }

    /**
     * Save a YAML file immediately (blocking).
     */
    public static void saveNow(YamlConfiguration config, File file) {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
