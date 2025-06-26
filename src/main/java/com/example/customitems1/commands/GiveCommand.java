// File: src/main/java/com/example/customitems1/commands/GiveCommand.java
package com.example.customitems1.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.example.customitems1.CustomItems1;

public class GiveCommand implements CommandExecutor {
    private final CustomItems1 plugin;

    public GiveCommand(CustomItems1 plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Usage: /givehopper <mobhopper|crophopper|autosell|xphopper>");
            return true;
        }
        String type = args[0].toLowerCase();
        String dispKey = switch (type) {
            case "mobhopper"   -> "item-names.mobhopper";
            case "crophopper"  -> "item-names.crophopper";
            case "autosell"    -> "item-names.autosell";
            case "xphopper"    -> "item-names.xphopper";
            default -> null;
        };
        if (dispKey == null) {
            sender.sendMessage("Unknown type: " + type);
            return true;
        }

        ItemStack item = new ItemStack(Material.HOPPER);
        ItemMeta meta = item.getItemMeta();
        String display = plugin.getCfg().getString(dispKey, type);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', display));
        // Tag it so the placement listener recognizes the type
        NamespacedKey key = new NamespacedKey(plugin, "custom_hopper_type");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, type);
        item.setItemMeta(meta);

        if (sender instanceof org.bukkit.entity.Player) {
            ((org.bukkit.entity.Player) sender).getInventory().addItem(item);
            sender.sendMessage("Gave you a " + display);
        } else {
            sender.sendMessage("Console cannot hold items!");
        }
        return true;
    }
}
