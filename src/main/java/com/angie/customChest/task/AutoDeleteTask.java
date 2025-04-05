package com.angie.customChest.task;

import com.angie.customChest.CustomChest;

import java.io.File;
import java.time.Instant;

public class AutoDeleteTask implements Runnable {

    private final CustomChest plugin;
    private final File folder;
    private final long keepSeconds;

    public AutoDeleteTask(CustomChest plugin) {
        this.plugin = plugin;

        String folderName = plugin.getSettingsManager().getAutoDeleteFolderName();
        this.folder = new File(plugin.getDataFolder(), folderName);
        this.keepSeconds = plugin.getSettingsManager().getAutoDeleteKeepSeconds();

        if (!folder.exists()) folder.mkdirs();
    }

    @Override
    public void run() {
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) return;

        long now = Instant.now().getEpochSecond();

        for (File file : files) {
            String name = file.getName();
            String[] parts = name.replace(".yml", "").split("_");
            if (parts.length < 3) continue;

            try {
                long timestamp = Long.parseLong(parts[2]);
                if ((now - timestamp) >= keepSeconds) {
                    if (file.delete()) {
                        plugin.getLogger().info("[AutoDelete] 오래된 백업 삭제됨: " + file.getName());
                    }
                }
            } catch (NumberFormatException ignored) {
            }
        }
    }
}
