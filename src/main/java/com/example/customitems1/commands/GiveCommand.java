package com.example.customitems1.commands;

import com.example.customitems1.CustomItems1;
import com.example.customitems1.ConfigManager;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class GiveCommand implements CommandExecutor, TabCompleter {
    private final CustomItems1 plugin;
    private final ConfigManager cfg;

    public GiveCommand(CustomItems1 plugin) {
        this.plugin = plugin;
        this.cfg = plugin.getCfg();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) return false;
        if (args.length < 2) {
            p.sendMessage(ChatColor.RED + "Usage: /ci give <autosell|crop|mob|xp> [amount]");
            return true;
        }

        String type = args[1].toLowerCase();
        int amount = args.length >= 3 ? Integer.parseInt(args[2]) : 1;
        ItemStack item;
        switch (type) {
            case "autosell":
                item = buildNamedItem(Material.CHEST, cfg.getAutoSellName(), "Sells configurable items");
                break;
            case "crop":
                item = buildNamedItem(Material.HOPPER, cfg.getCropHopperName(), cfg.getCropHopperItems());
                break;
            case "mob":
                item = buildNamedItem(Material.HOPPER, cfg.getMobHopperName(), cfg.getMobHopperItems());
                break;
            case "xp":
                item = buildNamedItem(Material.HOPPER, cfg.getXpHopperName(),
                    List.of("Converts XP to bottle every chunk"));
                break;
            default:
                p.sendMessage(ChatColor.RED + "Unknown type.");
                return true;
        }

        item.setAmount(amount);
        p.getInventory().addItem(item);
        p.sendMessage(ChatColor.GREEN + "Gave you " + amount + "x " + ChatColor.YELLOW + item.getItemMeta().getDisplayName());
        return true;
    }

    private ItemStack buildNamedItem(Material mat, String name, List<String> loreLines) {
        ItemStack stack = new ItemStack(mat);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        List<String> lore = new ArrayList<>();
        for (String L : loreLines) lore.add(ChatColor.GRAY + L);
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args) {
        if (args.length == 2) {
            return List.of("autosell", "crop", "mob", "xp");
        }
        return Collections.emptyList();
    }
}
