package org.eddie.kitsniEddie.Command;

import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.eddie.kitsniEddie.List.KitManager;

public class EddieChangeItemCommand implements CommandExecutor {

    private final KitManager kitManager;

    public EddieChangeItemCommand(KitManager kitManager) {
        this.kitManager = kitManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("kits.edit")) {
            player.sendMessage("§cYou don't have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§cUsage: /eddiechangeitem <category>");
            return true;
        }

        String category = args[0];
        if (!kitManager.getAllCategories().contains(category)) {
            player.sendMessage("§cThat category does not exist!");
            return true;
        }

        ItemStack held = player.getInventory().getItemInMainHand();
        if (held == null || held.getType() == Material.AIR) {
            player.sendMessage("§cYou must be holding an item to set as the display!");
            return true;
        }

        kitManager.setDisplayItem(category, held.clone());
        player.sendMessage("§aUpdated display item for category §e" + category + "§a!");
        return true;
    }
}