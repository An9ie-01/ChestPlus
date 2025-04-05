package com.angie.customChest;

import com.angie.customChest.command.StorageCommand;
import com.angie.customChest.command.StorageTabCompleter;
import com.angie.customChest.config.ConfigManager;
import com.angie.customChest.config.GuiConfigManager;
import com.angie.customChest.config.MessageManager;
import com.angie.customChest.config.SettingsManager;
import com.angie.customChest.data.StorageDataManager;
import com.angie.customChest.listener.StorageGUIListener;
import com.angie.customChest.listener.StorageQuitListener;
import com.angie.customChest.model.StorageHolder;
import com.angie.customChest.task.AutoDeleteTask;
import com.angie.customChest.task.AutoSaveTask;
import com.angie.customChest.util.StorageLockManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class CustomChest extends JavaPlugin {

    private static CustomChest instance;

    private ConfigManager configManager;
    private GuiConfigManager guiConfigManager;
    private MessageManager messageManager;
    private StorageDataManager storageDataManager;
    private SettingsManager settingsManager;
    private StorageLockManager storageLockManager;

    @Override
    public void onEnable() {
        instance = this;

        // 📁 기본 설정 파일 저장
        saveDefaultConfig();
        saveResource("gui.yml", false);
        saveResource("messages.yml", false);

        // ⚙️ 매니저 초기화 순서: 설정 → GUI/메시지 → 데이터
        configManager = new ConfigManager(this);
        guiConfigManager = new GuiConfigManager(this);
        messageManager = new MessageManager(this);
        storageDataManager = new StorageDataManager(this);
        settingsManager = new SettingsManager(this);

        // 🧹 캐시 초기화 (데이터 매니저 생성 후)
        storageDataManager.clearCache();
        storageLockManager = new StorageLockManager();

        // 🔗 명령어 등록
        getCommand("창고").setExecutor(new StorageCommand(this));
        getCommand("창고").setTabCompleter(new StorageTabCompleter());

        // 🎧 이벤트 등록
        getServer().getPluginManager().registerEvents(new StorageGUIListener(this), this);
        getServer().getPluginManager().registerEvents(new StorageQuitListener(this), this);

        // 💾 자동 저장
        if (settingsManager.isAutoSaveEnabled()) {
            long interval = settingsManager.getAutoSaveIntervalTicks();
            Bukkit.getScheduler().runTaskTimer(this, new AutoSaveTask(this), interval, interval);
            getLogger().info("자동 저장 기능이 활성화되었습니다.");
        }

        // 🧹 자동 삭제
        if (settingsManager.isAutoDeleteEnabled()) {
            long interval = 20L * 60 * 10; // 10분
            Bukkit.getScheduler().runTaskTimer(this, new AutoDeleteTask(this), interval, interval);
            getLogger().info("자동 삭제 기능이 활성화되었습니다.");
        }

        getLogger().info("CustomChest 플러그인이 활성화되었습니다!");
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

        getLogger().info("CustomChest 종료됨: 열린 창고 변경사항 저장 완료");
    }


    public static CustomChest getInstance() {
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
