package com.example.customitems1.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.example.customitems1.ConfigManager;
import com.example.customitems1.CustomItems1;

public class GiveCommand implements CommandExecutor {

    private final CustomItems1 plugin;
    private final ConfigManager cfg;

    public GiveCommand(CustomItems1 plugin) {
        this.plugin = plugin;
        this.cfg = plugin.getCfg();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd,
                             String label, String[] args) {

        if (!(sender instanceof Player p) || args.length < 2) return false;
        String type = args[1].toLowerCase();
        int amount = args.length > 2 ? Integer.parseInt(args[2]) : 1;

        Material mat;
        String name;
        switch (type) {
          case "autosell":
            mat = Material.CHEST;
            name = ChatColor.GOLD + "Auto-Sell Chest";
            plugin.getAutoSellChests(); break;
          case "crop":
            mat = Material.HOPPER;
            name = ChatColor.GREEN + "Crop Hopper";
            break;
          case "mob":
            mat = Material.HOPPER;
            name = ChatColor.DARK_RED + "Mob Drop Hopper";
            break;
          case "xp":
            mat = Material.HOPPER;
            name = ChatColor.AQUA + "XP Hopper";
            break;
          default:
            return false;
        }

        ItemStack item = new ItemStack(mat, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        p.getInventory().addItem(item);
        p.sendMessage(ChatColor.YELLOW + "Gave you " + amount + "× " + name);
        return true;
    }
}
