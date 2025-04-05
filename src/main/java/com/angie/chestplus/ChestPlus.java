package com.angie.chestplus;

import com.angie.chestplus.command.StorageCommand;
import com.angie.chestplus.command.StorageTabCompleter;
import com.angie.chestplus.config.ConfigManager;
import com.angie.chestplus.config.GuiConfigManager;
import com.angie.chestplus.config.MessageManager;
import com.angie.chestplus.config.SettingsManager;
import com.angie.chestplus.data.StorageDataManager;
import com.angie.chestplus.listener.StorageGUIListener;
import com.angie.chestplus.listener.StorageQuitListener;
import com.angie.chestplus.model.StorageHolder;
import com.angie.chestplus.task.AutoDeleteTask;
import com.angie.chestplus.task.AutoSaveTask;
import com.angie.chestplus.util.StorageLockManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class ChestPlus extends JavaPlugin {

    private static ChestPlus instance;

    private ConfigManager configManager;
    private GuiConfigManager guiConfigManager;
    private MessageManager messageManager;
    private StorageDataManager storageDataManager;
    private SettingsManager settingsManager;
    private StorageLockManager storageLockManager;

    @Override
    public void onEnable() {
        instance = this;

        // 📁 Save default files
        saveDefaultConfig();
        saveResource("gui.yml", false);
        saveResource("messages.yml", false);

        // ⚙️ Initialize managers
        configManager = new ConfigManager(this);
        guiConfigManager = new GuiConfigManager(this);
        messageManager = new MessageManager(this);
        storageDataManager = new StorageDataManager(this);
        settingsManager = new SettingsManager(this);
        storageLockManager = new StorageLockManager();

        storageDataManager.clearCache();

        // 🔗 Register command
        getCommand("chest").setExecutor(new StorageCommand(this));
        getCommand("chest").setTabCompleter(new StorageTabCompleter());

        // 🎧 Register events
        getServer().getPluginManager().registerEvents(new StorageGUIListener(this), this);
        getServer().getPluginManager().registerEvents(new StorageQuitListener(this), this);

        // 💾 Auto-save
        if (settingsManager.isAutoSaveEnabled()) {
            long interval = settingsManager.getAutoSaveIntervalTicks();
            Bukkit.getScheduler().runTaskTimer(this, new AutoSaveTask(this), interval, interval);
            getLogger().info("Auto-save enabled.");
        }

        // 🧹 Auto-delete
        if (settingsManager.isAutoDeleteEnabled()) {
            long interval = 20L * 60 * 10;
            Bukkit.getScheduler().runTaskTimer(this, new AutoDeleteTask(this), interval, interval);
            getLogger().info("Auto-delete enabled.");
        }

        getLogger().info("ChestPlus has been enabled.");
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Inventory topInv = player.getOpenInventory().getTopInventory();

            if (!(topInv.getHolder() instanceof StorageHolder holder)) continue;
            if (!holder.getGuiKey().startsWith("CHEST_")) continue;

            UUID owner = holder.getOwnerUUID();
            int chestId = holder.getChestId();
            ItemStack[] now = topInv.getContents();

            String key = owner + ":" + chestId;
            ItemStack[] last = getStorageDataManager().getLastSavedCache(key);

            if (!getStorageDataManager().hasChanged(last, now)) continue;

            getStorageDataManager().saveSync(owner, chestId, now);
            getStorageDataManager().updateCache(key, now);
            getStorageLockManager().unlock(owner, chestId);
        }

        getLogger().info("ChestPlus has been disabled. All open chests saved.");
    }

    public static ChestPlus getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public GuiConfigManager getGuiConfigManager() {
        return guiConfigManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public StorageDataManager getStorageDataManager() {
        return storageDataManager;
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }

    public StorageLockManager getStorageLockManager() {
        return storageLockManager;
    }

    public void setGuiConfigManager(GuiConfigManager guiConfigManager) {
        this.guiConfigManager = guiConfigManager;
    }

    public void setMessageManager(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }
}
