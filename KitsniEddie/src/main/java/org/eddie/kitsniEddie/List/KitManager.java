package org.eddie.kitsniEddie.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.eddie.kitsniEddie.KitsniEddie;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class KitManager {

    private final Map<String, ItemStack[]> kits = new HashMap<>();
    private final Map<String, ItemStack> displayItems = new HashMap<>();
    private final File file = new File(KitsniEddie.getInstance().getDataFolder(), "kits.yml");
    private FileConfiguration config;

    public KitManager() {
        if (!file.exists()) {
            KitsniEddie.getInstance().saveResource("kits.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void setKit(String category, ItemStack[] items) {
        kits.put(category, items);
        config.set(category + ".items", Arrays.asList(items));
        saveConfig();
    }

    public ItemStack[] getKit(String category) {
        return kits.getOrDefault(category, new ItemStack[0]);
    }

    public Set<String> getAllCategories() {
        return kits.keySet();
    }

    public void loadKits() {
        kits.clear();
        displayItems.clear();
        config = YamlConfiguration.loadConfiguration(file);

        for (String category : config.getKeys(false)) {
            List<?> list = config.getList(category + ".items");
            if (list != null) {
                kits.put(category, list.toArray(new ItemStack[0]));
            } else {
                kits.put(category, new ItemStack[0]);
            }

            ItemStack display = config.getItemStack(category + ".display");
            if (display != null) {
                displayItems.put(category, display);
            }
        }
    }

    private void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeKit(String category) {
        kits.remove(category);
        displayItems.remove(category);
        config.set(category, null);
        saveConfig();
    }

    public void setDisplayItem(String category, ItemStack item) {
        displayItems.put(category, item);
        config.set(category + ".display", item);
        saveConfig();
    }

    public ItemStack getDisplayItem(String category) {
        return displayItems.getOrDefault(category, new ItemStack(Material.IRON_SWORD));
    }
}
