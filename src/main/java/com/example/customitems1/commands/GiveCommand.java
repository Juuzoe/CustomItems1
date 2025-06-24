package com.example.customitems1.commands;

import java.util.ArrayList;
import java.util.List;

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
        this.cfg    = plugin.getCfg();
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             Command cmd,
                             String label,
                             String[] args) {
        if (args.length < 2 || !args[0].equalsIgnoreCase("give")) {
            sender.sendMessage(ChatColor.RED + "Usage: /ci give <autosell|crop|mob|xp> [amount]");
            return true;
        }

        String type = args[1].toLowerCase();
        int amount = 1;
        if (args.length >= 3) {
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid number: " + args[2]);
                return true;
            }
        }

        ItemStack stack;
        String displayName;
        List<String> lore = new ArrayList<>();

        switch (type) {
            case "autosell":
            case "autosellchest":
                stack = new ItemStack(Material.CHEST, amount);
                displayName = ChatColor.GOLD + cfg.getAutoSellName();
                lore.add(ChatColor.GRAY + "Auto-sells these items:");
                for (String mat : cfg.getAutoSellItems()) {
                    lore.add(ChatColor.WHITE + " • " + mat);
                }
                lore.add(ChatColor.GRAY + "Tax: " + cfg.getAutoSellTaxPercent() + "%");
                break;

            case "crop":
            case "crophopper":
                stack = new ItemStack(Material.HOPPER, amount);
                displayName = ChatColor.GREEN + cfg.getCropHopperName();
                lore.add(ChatColor.GRAY + "Collects crops in the chunk:");
                for (String mat : cfg.getCropHopperLoot()) {
                    lore.add(ChatColor.WHITE + " • " + mat);
                }
                break;

            case "mob":
            case "mobhopper":
                stack = new ItemStack(Material.HOPPER, amount);
                displayName = ChatColor.DARK_RED + cfg.getMobHopperName();
                lore.add(ChatColor.GRAY + "Collects mob drops in the chunk:");
                for (String mat : cfg.getMobHopperLoot()) {
                    lore.add(ChatColor.WHITE + " • " + mat);
                }
                break;

            case "xp":
            case "xphopper":
                stack = new ItemStack(Material.HOPPER, amount);
                displayName = ChatColor.AQUA + cfg.getXpHopperName();
                lore.add(ChatColor.GRAY + "Collects XP orbs and bottles them.");
                break;

            default:
                sender.sendMessage(ChatColor.RED + "Unknown type: " + type);
                return true;
        }

        // Apply meta
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        stack.setItemMeta(meta);

        // Give to player or drop at spawn
        if (sender instanceof Player p) {
            p.getInventory().addItem(stack);
        } else {
            plugin.getServer()
                  .getWorlds().get(0)
                  .dropItemNaturally(
                    plugin.getServer().getWorlds().get(0).getSpawnLocation(),
                    stack
                  );
        }

        sender.sendMessage(
          ChatColor.GREEN + "Gave " + amount + "× " + ChatColor.RESET + displayName
        );
        return true;
    }
}
