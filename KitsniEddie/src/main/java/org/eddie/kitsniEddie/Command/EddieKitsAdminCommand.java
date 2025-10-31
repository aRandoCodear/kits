package org.eddie.kitsniEddie.Command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.eddie.kitsniEddie.List.InventoryListeners;
import org.eddie.kitsniEddie.List.KitManager;

public class EddieKitsAdminCommand implements CommandExecutor {

    private final KitManager kitManager;

    public EddieKitsAdminCommand(KitManager kitManager) {
        this.kitManager = kitManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage("Usage: /eddiekitsadmin <category>");
            return true;
        }

        String category = args[0];

        ItemStack[] kitItems = kitManager.getKit(category);
        if (kitItems == null) {
            kitItems = new ItemStack[54];
            kitManager.setKit(category, kitItems);
        }

        Inventory inv = Bukkit.createInventory(null, 54, "§6Editing Category: " + category);

        for (int i = 0; i < kitItems.length && i < 45; i++) {
            if (kitItems[i] != null) inv.setItem(i, kitItems[i]);
        }

        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");
        filler.setItemMeta(fillerMeta);
        for (int i = 45; i < 54; i++) {
            inv.setItem(i, filler);
        }

        ItemStack refill = new ItemStack(Material.SLIME_BALL);
        ItemMeta refillMeta = refill.getItemMeta();
        refillMeta.setDisplayName("§aRefill");
        refill.setItemMeta(refillMeta);
        inv.setItem(48, refill);

        ItemStack done = new ItemStack(Material.BARRIER);
        ItemMeta doneMeta = done.getItemMeta();
        doneMeta.setDisplayName("§cDone");
        done.setItemMeta(doneMeta);
        inv.setItem(50, done);

        player.openInventory(inv);

        InventoryListeners.startEditing(player, category);
        player.sendMessage("§aEditing category '" + category + "'.");

        return true;
    }
}