package org.eddie.kitsniEddie.Command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.eddie.kitsniEddie.List.KitManager;

public class EddieKitsCommand implements CommandExecutor {

    private final KitManager kitManager;

    public EddieKitsCommand(KitManager kitManager) {
        this.kitManager = kitManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        openCategoryGUI(player);
        return true;
    }

    private void openCategoryGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6Kit Categories");

        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");
        filler.setItemMeta(fillerMeta);

        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, filler);
        }

        int[] categorySlots = {
                10, 12, 14, 16,
                19, 21, 23, 25,
                28, 30, 32, 34
        };

        int index = 0;
        for (String category : kitManager.getAllCategories()) {
            if (index >= categorySlots.length) break;

            ItemStack icon = kitManager.getDisplayItem(category).clone();
            if (icon == null || icon.getType() == Material.AIR) {
                icon = new ItemStack(Material.IRON_SWORD); // fallback
            }

            ItemMeta meta = icon.getItemMeta();
            meta.setDisplayName("§e" + category);
            icon.setItemMeta(meta);

            gui.setItem(categorySlots[index++], icon);
        }

        ItemStack titleItem = new ItemStack(Material.NAME_TAG);
        ItemMeta titleMeta = titleItem.getItemMeta();
        titleMeta.setDisplayName("§6§lSelect a Kit Category");
        titleItem.setItemMeta(titleMeta);
        gui.setItem(4, titleItem);

        player.openInventory(gui);
    }
}
