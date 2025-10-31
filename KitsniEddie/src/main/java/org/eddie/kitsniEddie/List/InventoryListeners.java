package org.eddie.kitsniEddie.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class InventoryListeners implements Listener {

    private final KitManager kitManager;
    private static final Map<Player, String> editing = new HashMap<>();
    private static final Map<Player, ItemStack[]> openedKits = new HashMap<>();

    public InventoryListeners(KitManager kitManager) {
        this.kitManager = kitManager;
    }

    public static void startEditing(Player player, String category) {
        editing.put(player, category);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();
        String title = e.getView().getTitle();

        if (title.startsWith("§6Editing Category: ")) {
            Inventory top = e.getView().getTopInventory();
            int size = top.getSize();
            int slot = e.getRawSlot();
            int lastRowStart = size - 9;

            if (slot >= lastRowStart && slot < size) {
                e.setCancelled(true);
                return;
            }

            ItemStack clicked = e.getCurrentItem();
            if (clicked != null && clicked.hasItemMeta()) {
                String name = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
                if (name.equalsIgnoreCase("Refill") || name.equalsIgnoreCase("Done")) {
                    e.setCancelled(true);
                }
            }

            return;
        }

        if (title.equals("§6Kit Categories")) {
            e.setCancelled(true);
            ItemStack clicked = e.getCurrentItem();
            if (clicked == null || !clicked.hasItemMeta()) return;

            String category = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
            if (category.isEmpty()) return;

            openKitSelectionGUI(player, category);
            return;
        }

        if (title.startsWith("§6Select Items: ")) {
            Inventory top = e.getView().getTopInventory();
            ItemStack clicked = e.getCurrentItem();
            if (clicked == null) return;

            if (e.getClickedInventory() == top) {
                if (!clicked.hasItemMeta()) return;
                String name = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());

                if (clicked.getType() == Material.GRAY_STAINED_GLASS_PANE) {
                    e.setCancelled(true);
                    return;
                }

                if (name.equalsIgnoreCase("Done")) {
                    player.closeInventory();
                    player.sendMessage("§aKit closed.");
                    e.setCancelled(true);
                    return;
                }

                if (name.equalsIgnoreCase("Refill")) {
                    String category = title.substring("§6Select Items: ".length());
                    openKitSelectionGUI(player, category);
                    player.sendMessage("§aKit refilled.");
                    e.setCancelled(true);
                    return;
                }

                e.setCancelled(false);
            }
        }
    }

    private void openKitSelectionGUI(Player player, String category) {
        ItemStack[] kitItems = kitManager.getKit(category);
        if (kitItems == null || kitItems.length == 0) {
            player.sendMessage("§cThis category has no items.");
            return;
        }

        int itemCount = kitItems.length;
        int size = ((itemCount + 8) / 9) * 9;
        size = Math.max(9, Math.min(54, size));

        Inventory inv = Bukkit.createInventory(null, size, "§6Select Items: " + category);

        int maxItems = Math.min(size - 9, kitItems.length);
        for (int i = 0; i < maxItems; i++) {
            if (kitItems[i] != null) inv.setItem(i, kitItems[i].clone());
        }

        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");
        filler.setItemMeta(fillerMeta);
        for (int i = size - 9; i < size; i++) {
            inv.setItem(i, filler);
        }

        ItemStack refill = new ItemStack(Material.SLIME_BALL);
        ItemMeta refillMeta = refill.getItemMeta();
        refillMeta.setDisplayName("§aRefill");
        refill.setItemMeta(refillMeta);
        inv.setItem(size - 6, refill);

        ItemStack done = new ItemStack(Material.BARRIER);
        ItemMeta doneMeta = done.getItemMeta();
        doneMeta.setDisplayName("§cDone");
        done.setItemMeta(doneMeta);
        inv.setItem(size - 4, done);

        openedKits.put(player, kitItems);
        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) return;
        Player player = (Player) e.getPlayer();

        openedKits.remove(player);

        // Save category contents when editing
        if (editing.containsKey(player)) {
            String category = editing.get(player);
            ItemStack[] contents = e.getInventory().getContents();
            kitManager.setKit(category, contents);
            player.sendMessage("§aSaved category '" + category + "'.");
            editing.remove(player);
        }
    }
}