package com.angie.customChest.config;

import com.angie.customChest.CustomChest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.*;

public class GuiConfigManager {

    private final CustomChest plugin;
    private final YamlConfiguration config;

    public GuiConfigManager(CustomChest plugin) {
        this.plugin = plugin;
        File file = new File(plugin.getDataFolder(), "gui.yml");
        if (!file.exists()) {
            plugin.saveResource("gui.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public int getGuiSize() {
        return config.getInt("main-gui.size", 27);
    }

    public String getGuiTitle() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("main-gui.title", "&6창고 선택"));
    }

    public Map<Integer, String> getSlotIconMap() {
        Map<Integer, String> map = new HashMap<>();
        ConfigurationSection section = config.getConfigurationSection("main-gui.slots");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                try {
                    int slot = Integer.parseInt(key);
                    String iconKey = section.getString(key);
                    map.put(slot, iconKey);
                } catch (NumberFormatException ignored) {}
            }
        }
        return map;
    }

    public ItemStack getIconItem(String iconKey, Player player) {
        ConfigurationSection section = config.getConfigurationSection("icons." + iconKey);
        if (section == null) return new ItemStack(Material.BARRIER);

        String materialName = section.getString("material", "BARRIER");
        Material material = Material.matchMaterial(materialName.toUpperCase());
        if (material == null) material = Material.BARRIER;

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            String name = section.getString("name");
            if (name != null) meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

            List<String> lore = section.getStringList("lore");
            if (lore != null && !lore.isEmpty()) {
                List<String> coloredLore = new ArrayList<>();
                for (String line : lore) {
                    coloredLore.add(ChatColor.translateAlternateColorCodes('&', line));
                }
                meta.setLore(coloredLore);
            }
            item.setItemMeta(meta);
        }

        String permission = section.getString("permission");
        if (permission != null && !player.hasPermission(permission)) {
            item.setType(Material.BARRIER);
            ItemMeta meta2 = item.getItemMeta();
            if (meta2 != null) {
                meta2.setDisplayName(ChatColor.RED + "잠김");
                meta2.setLore(Collections.singletonList(ChatColor.GRAY + "권한이 없습니다."));
                item.setItemMeta(meta2);
            }
        }

        return item;
    }
}
