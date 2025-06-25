package com.example.customitems1.listeners;

import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.example.customitems1.AutoSellChest;
import com.example.customitems1.CustomItems1;

public class AutoSellChestListener implements Listener {

    private final CustomItems1 plugin;
    private final FileConfiguration cfg;

    public AutoSellChestListener(CustomItems1 plugin) {
        this.plugin = plugin;
        this.cfg = plugin.getCfg();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // 1) Ensure this is a chest inventory
        if (!(event.getView().getTopInventory().getHolder() instanceof Chest)) {
            return;
        }

        // 2) Get its block location
        Chest chestBlock = (Chest) event.getView().getTopInventory().getHolder();
        Location loc = chestBlock.getBlock().getLocation();

        // 3) Lookup our chest stub
        AutoSellChest asc = plugin.getAutoSellChests().get(loc);
        if (asc != null) {
            // TODO: implement auto‐sell logic here
        }
    }
}
