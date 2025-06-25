package com.example.customitems1;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import com.example.customitems1.commands.GiveCommand;
import com.example.customitems1.listeners.AutoSellChestListener;
import com.example.customitems1.listeners.CropHopperListener;
import com.example.customitems1.listeners.MobDropHopperListener;
import com.example.customitems1.listeners.XPHopperListener;

public class CustomItems1 extends JavaPlugin {

    private ConfigManager cfg;

    /** track all of your custom‐item block locations */
    private final Map<Location, UUID> mobHoppers     = new HashMap<>();
    private final Map<Location, UUID> cropHoppers    = new HashMap<>();
    private final Map<Location, UUID> xpHoppers      = new HashMap<>();
    private final Map<Location, Boolean> autoSellChests = new HashMap<>();

    @Override
    public void onEnable() {
        this.cfg = new ConfigManager(this);

        // (Optionally) load existing locations from disk/config here…

        // register listeners & commands
        getServer().getPluginManager().registerEvents(new AutoSellChestListener(this), this);
        getServer().getPluginManager().registerEvents(new MobDropHopperListener(this), this);
        getServer().getPluginManager().registerEvents(new CropHopperListener(this), this);
        getServer().getPluginManager().registerEvents(new XPHopperListener(this), this);

        getCommand("ci").setExecutor(new GiveCommand(this));
    }

    @Override
    public void onDisable() {
        // (Optionally) save your hopper maps out to config here…
    }

    // getters for your listeners
    public ConfigManager getCfg() {
        return cfg;
    }

    public Map<Location, UUID> getMobHoppers() {
        return mobHoppers;
    }

    public Map<Location, UUID> getCropHoppers() {
        return cropHoppers;
    }

    public Map<Location, UUID> getXpHoppers() {
        return xpHoppers;
    }

    public Map<Location, Boolean> getAutoSellChests() {
        return autoSellChests;
    }
}
