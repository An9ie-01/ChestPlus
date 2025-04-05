package com.angie.chestplus.gui;

import com.angie.chestplus.ChestPlus;
import com.angie.chestplus.config.GuiConfigManager;
import com.angie.chestplus.model.StorageHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * Displays the main GUI where the player selects a storage chest.
 */
public class MainStorageGUI {

    private final ChestPlus plugin;

    public MainStorageGUI(ChestPlus plugin) {
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
