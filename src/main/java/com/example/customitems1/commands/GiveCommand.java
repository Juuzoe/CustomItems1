// File: src/main/java/com/example/customitems1/commands/GiveCommand.java
package com.example.customitems1.commands;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.example.customitems1.CustomItems1;

public class GiveCommand implements CommandExecutor {
    private final CustomItems1 plugin;
    private final NamespacedKey hopperKey;

    public GiveCommand(CustomItems1 plugin) {
        this.plugin = plugin;
        this.hopperKey = new NamespacedKey(plugin, "custom_hopper_type");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may use this command.");
            return true;
        }
        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage("§cUsage: /givehopper <mobdrop|crophopper|autosell|xphopper>");
            return true;
        }

        String type = args[0].toLowerCase();
        ItemStack item;

        switch (type) {
            case "mobdrop":
                item = createHopperItem(Material.HOPPER,
                        "§aMob Drop Hopper",
                        "Collects all mob drops in this chunk",
                        "mobdrop");
                break;
            case "crophopper":
                item = createHopperItem(Material.HOPPER,
                        "§aCrop Hopper",
                        "Collects all crop drops in this chunk",
                        "crophopper");
                break;
            case "autosell":
                item = createHopperItem(Material.CHEST,
                        "§aAutoSell Chest",
                        "Auto-sells items placed inside",
                        "autosell");
                break;
            case "xphopper":
                item = createHopperItem(Material.HOPPER,
                        "§aXP Hopper",
                        "Collects XP in this chunk",
                        "xphopper");
                break;
            default:
                player.sendMessage("§cUnknown hopper type: " + type);
                return true;
        }

        player.getInventory().addItem(item);
        player.sendMessage("§aYou have been given a " +
                item.getItemMeta().getDisplayName() + "§a.");
        return true;
    }

    private ItemStack createHopperItem(Material material, String name, String lore, String id) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore, "§7Place to activate"));
        meta.getPersistentDataContainer().set(hopperKey,
                PersistentDataType.STRING, id);
        stack.setItemMeta(meta);
        return stack;
    }
}
