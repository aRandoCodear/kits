package org.eddie.kitsniEddie;

import org.bukkit.plugin.java.JavaPlugin;
import org.eddie.kitsniEddie.Command.EddieChangeItemCommand;
import org.eddie.kitsniEddie.Command.EddieKitRemoverCommand;
import org.eddie.kitsniEddie.Command.EddieKitsAdminCommand;
import org.eddie.kitsniEddie.Command.EddieKitsCommand;
import org.eddie.kitsniEddie.List.InventoryListeners;
import org.eddie.kitsniEddie.List.KitManager;

import java.io.File;

public final class KitsniEddie extends JavaPlugin {

    private static KitsniEddie instance;
    private KitManager kitManager;

    @Override
    public void onEnable() {
        instance = this;

        File kitsFile = new File(getDataFolder(), "kits.yml");
        if (!kitsFile.exists()) {
            saveResource("kits.yml", false);
        }

        kitManager = new KitManager();
        kitManager.loadKits();

        getCommand("eddiekits").setExecutor(new EddieKitsCommand(kitManager));
        getCommand("eddiekitsadmin").setExecutor(new EddieKitsAdminCommand(kitManager));
        getCommand("eddiekitremove").setExecutor(new EddieKitRemoverCommand(kitManager));
        getCommand("eddiechangeitem").setExecutor(new EddieChangeItemCommand(kitManager));
        getServer().getPluginManager().registerEvents(new InventoryListeners(kitManager), this);
        getLogger().info("✅ KitsniEddie enabled successfully!");
    }

    public static KitsniEddie getInstance() {
        return instance;
    }

    public KitManager getKitManager() {
        return kitManager;
    }
}