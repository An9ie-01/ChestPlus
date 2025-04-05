package com.angie.chestplus.task;

import com.angie.chestplus.ChestPlus;
import com.angie.chestplus.model.StorageHolder;
import com.angie.chestplus.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.time.Instant;
import java.util.UUID;

public class AutoSaveTask implements Runnable {

    private final ChestPlus plugin;
    private final File autosaveFolder;

    public AutoSaveTask(ChestPlus plugin) {
        this.plugin = plugin;
        this.autosaveFolder = new File(plugin.getDataFolder(), plugin.getSettingsManager().getAutoDeleteFolderName());
        if (!autosaveFolder.exists()) autosaveFolder.mkdirs();
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Inventory topInv = player.getOpenInventory().getTopInventory();

            if (!(topInv.getHolder() instanceof StorageHolder holder)) continue;
            if (!holder.getGuiKey().startsWith("CHEST_")) continue;

            int chestId;
            try {
                chestId = Integer.parseInt(holder.getGuiKey().replace("CHEST_", ""));
            } catch (NumberFormatException e) {
                continue;
            }

            UUID uuid = holder.getOwnerUUID();
            long timestamp = Instant.now().getEpochSecond();
            File file = new File(autosaveFolder, uuid + "_" + chestId + "_" + timestamp + ".yml");

            YamlConfiguration config = new YamlConfiguration();
            config.set("uuid", uuid.toString());
            config.set("chest-id", chestId);
            config.set("timestamp", timestamp);
            config.set("contents", topInv.getContents());

            FileUtil.saveAsync(plugin, config, file);
        }
    }
}
