package com.example.customitems1;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.example.customitems1.commands.GiveCommand;
import com.example.customitems1.listeners.AutoSellChestListener;
import com.example.customitems1.listeners.CropHopperListener;
import com.example.customitems1.listeners.MobDropHopperListener;
import com.example.customitems1.listeners.XPHopperListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;

public class CustomItems1 extends JavaPlugin {

    private ConfigManager cfg;

    // chest → owner UUID
    private final Map<Location, UUID> autoSellChests = new HashMap<>();
    private final Map<Location, UUID> cropHoppers    = new HashMap<>();
    private final Map<Location, UUID> mobHoppers     = new HashMap<>();
    private final Map<Location, UUID> xpHoppers      = new HashMap<>();

    @Override
    public void onEnable() {
        // load or create config
        saveDefaultConfig();
        this.cfg = new ConfigManager(this);

        // register commands & listeners
        this.getCommand("ci").setExecutor(new GiveCommand(this));
        Bukkit.getPluginManager().registerEvents(new AutoSellChestListener(this), this);
        Bukkit.getPluginManager().registerEvents(new CropHopperListener(this), this);
        Bukkit.getPluginManager().registerEvents(new MobDropHopperListener(this), this);
        Bukkit.getPluginManager().registerEvents(new XPHopperListener(this), this);
    }

    public ConfigManager getCfg() {
        return cfg;
    }

    public Map<Location, UUID> getAutoSellChests() {
        return autoSellChests;
    }
    public Map<Location, UUID> getCropHoppers() {
        return cropHoppers;
    }
    public Map<Location, UUID> getMobHoppers() {
        return mobHoppers;
    }
    public Map<Location, UUID> getXpHoppers() {
        return xpHoppers;
    }
}
