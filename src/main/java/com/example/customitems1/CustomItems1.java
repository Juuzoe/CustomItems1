package com.example.customitems1;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.example.customitems1.commands.GiveCommand;
import com.example.customitems1.listeners.AutoSellChestListener;
import com.example.customitems1.listeners.CropHopperListener;
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
        // Save default config if missing
        saveDefaultConfig();
        this.cfg = getConfig();

        // Register listeners
        getServer().getPluginManager().registerEvents(new MobDropHopperListener(this), this);
        getServer().getPluginManager().registerEvents(new CropHopperListener(this), this);
        getServer().getPluginManager().registerEvents(new AutoSellChestListener(this), this);
        getServer().getPluginManager().registerEvents(new XPHopperListener(this), this);

        // Register commands
        if (getCommand("givehopper") != null) {
            getCommand("givehopper").setExecutor(new GiveCommand(this));
        }
    }

    /** Access to the loaded configuration */
    public FileConfiguration getCfg() {
        return cfg;
    }

    /** All active MobDropHoppers by chunk-center location */
    public Map<Location, MobDropHopper> getMobHoppers() {
        return mobHoppers;
    }

    /** All active CropHoppers by chunk-center location */
    public Map<Location, CropHopper> getCropHoppers() {
        return cropHoppers;
    }

    /** All active AutoSellChests by chest location */
    public Map<Location, AutoSellChest> getAutoSellChests() {
        return autoSellChests;
    }

    /** All active XPHoppers by chunk-center location */
    public Map<Location, XPHopper> getXpHoppers() {
        return xpHoppers;
    }
}
