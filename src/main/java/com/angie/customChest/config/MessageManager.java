package com.angie.customChest.config;

import com.angie.customChest.CustomChest;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MessageManager {

    private final CustomChest plugin;
    private final YamlConfiguration config;
    private final Map<String, String> cache = new HashMap<>();
    private final String prefix;

    public MessageManager(CustomChest plugin) {
        this.plugin = plugin;
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);

        this.prefix = ChatColor.translateAlternateColorCodes('&',
                config.getString("prefix", "&7[&6CustomChest&7] ")) + " ";
    }

    public String get(String key) {
        if (cache.containsKey(key)) return cache.get(key);

        String raw = config.getString(key, "&c정의되지 않은 메시지: " + key);
        raw = raw.replace("%prefix%", prefix);
        raw = ChatColor.translateAlternateColorCodes('&', raw);

        cache.put(key, raw);
        return raw;
    }

    public String getRaw(String key) {
        return config.getString(key);
    }

    public String getPrefix() {
        return prefix;
    }
}
