package com.angie.chestplus.task;

import com.angie.chestplus.ChestPlus;

import java.io.File;
import java.time.Instant;

public class AutoDeleteTask implements Runnable {

    private final ChestPlus plugin;
    private final File folder;
    private final long keepSeconds;

    public AutoDeleteTask(ChestPlus plugin) {
        this.plugin = plugin;
        this.folder = new File(plugin.getDataFolder(), plugin.getSettingsManager().getAutoDeleteFolderName());
        this.keepSeconds = plugin.getSettingsManager().getAutoDeleteKeepSeconds();

        if (!folder.exists()) folder.mkdirs();
    }

    @Override
    public void run() {
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) return;

        long now = Instant.now().getEpochSecond();

        for (File file : files) {
            String[] parts = file.getName().replace(".yml", "").split("_");
            if (parts.length < 3) continue;

            try {
                long timestamp = Long.parseLong(parts[2]);
                if ((now - timestamp) >= keepSeconds && file.delete()) {
                    plugin.getLogger().info("[AutoDelete] Deleted old backup: " + file.getName());
                }
            } catch (NumberFormatException ignored) {}
        }
    }
}
