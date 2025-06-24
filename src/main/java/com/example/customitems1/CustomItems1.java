package com.example.customitems1;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.example.customitems1.commands.GiveCommand;
import com.example.customitems1.listeners.AutoSellChestListener;
import com.example.customitems1.listeners.CropHopperListener;
import com.example.customitems1.listeners.MobDropHopperListener;
import com.example.customitems1.listeners.XPHopperListener;

public class CustomItems1 extends JavaPlugin {

    private ConfigManager cfg;
    private final Map<Location, UUID> autoSellChests = new HashMap<>();
    private final Map<Location, UUID> cropHoppers    = new HashMap<>();
    private final Map<Location, UUID> mobHoppers     = new HashMap<>();
    private final Map<Location, UUID> xpHoppers      = new HashMap<>();

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

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.cfg = new ConfigManager(this);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new AutoSellChestListener(this), this);
        pm.registerEvents(new CropHopperListener(this), this);
        pm.registerEvents(new MobDropHopperListener(this), this);
        pm.registerEvents(new XPHopperListener(this), this);

        this.getCommand("ci").setExecutor(new GiveCommand(this));

        getLogger().info("CustomItems1 enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("CustomItems1 disabled.");
    }
}
