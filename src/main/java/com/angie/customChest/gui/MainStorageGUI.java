package com.angie.customChest.gui;

import com.angie.customChest.CustomChest;
import com.angie.customChest.config.GuiConfigManager;
import com.angie.customChest.model.StorageHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MainStorageGUI {

    private final CustomChest plugin;

    public MainStorageGUI(CustomChest plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        GuiConfigManager guiConfig = plugin.getGuiConfigManager();

        int size = guiConfig.getGuiSize();
        String title = guiConfig.getGuiTitle();

        Inventory gui = Bukkit.createInventory(
                new StorageHolder("MAIN", player.getUniqueId(), -1),
                size,
                title
        );

        guiConfig.getSlotIconMap().forEach((slot, iconKey) -> {
            gui.setItem(slot, guiConfig.getIconItem(iconKey, player));
        });

        player.openInventory(gui);
    }
}
