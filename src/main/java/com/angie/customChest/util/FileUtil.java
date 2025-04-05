package com.angie.customChest.util;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    public static void saveAsync(Plugin plugin, YamlConfiguration config, File file) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                config.save(file);
            } catch (IOException e) {
                plugin.getLogger().warning("파일 저장 실패: " + file.getName());
                e.printStackTrace();
            }
        });
    }

    public static void saveNow(YamlConfiguration config, File file) {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
