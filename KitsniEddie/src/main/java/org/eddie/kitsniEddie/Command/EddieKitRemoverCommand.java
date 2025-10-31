package org.eddie.kitsniEddie.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eddie.kitsniEddie.List.KitManager;

public class EddieKitRemoverCommand implements CommandExecutor {
    private final KitManager kitManager;

    public EddieKitRemoverCommand(KitManager kitManager) {
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
            player.sendMessage("§cYou don't have permission to do that.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§cUsage: /removekit <category>");
            return true;
        }

        String category = args[0];
        if (!kitManager.getAllCategories().contains(category)) {
            player.sendMessage("§cThat category does not exist!");
            return true;
        }

        kitManager.removeKit(category);
        player.sendMessage("§aCategory §e" + category + " §ahas been removed!");
        return true;
    }
}
