// File: src/main/java/com/example/customitems1/CustomItems1.java
package com.example.customitems1;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.example.customitems1.commands.GiveCommand;
import com.example.customitems1.listeners.AutoSellChestListener;
import com.example.customitems1.listeners.CropHopperListener;
import com.example.customitems1.listeners.HopperPlacementListener;
import com.example.customitems1.listeners.MobDropHopperListener;
import com.example.customitems1.listeners.XPHopperListener;

public class CustomItems1 extends JavaPlugin {
    private FileConfiguration cfg;

    private final Map<Location, MobDropHopper> mobHoppers = new HashMap<>();
    private final Map<Location, CropHopper> cropHoppers = new HashMap<>();
    private final Map<Location, AutoSellChest> autoSellChests = new HashMap<>();
    private final Map<Location, XPHopper> xpHoppers = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        cfg = getConfig();

        getCommand("givehopper").setExecutor(new GiveCommand(this));

        getServer().getPluginManager().registerEvents(
            new HopperPlacementListener(this), this);
        getServer().getPluginManager().registerEvents(
            new MobDropHopperListener(this), this);
        getServer().getPluginManager().registerEvents(
            new CropHopperListener(this), this);
        getServer().getPluginManager().registerEvents(
            new AutoSellChestListener(this), this);
        getServer().getPluginManager().registerEvents(
            new XPHopperListener(this), this);
    }

    public FileConfiguration getCfg() {
        return cfg;
    }

    public Map<Location, MobDropHopper> getMobHoppers() {
        return mobHoppers;
    }

    public Map<Location, CropHopper> getCropHoppers() {
        return cropHoppers;
    }

    public Map<Location, AutoSellChest> getAutoSellChests() {
        return autoSellChests;
    }

    public Map<Location, XPHopper> getXpHoppers() {
        return xpHoppers;
    }
}
