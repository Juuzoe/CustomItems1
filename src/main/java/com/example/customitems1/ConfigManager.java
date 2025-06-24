package com.example.customitems1;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

    private final FileConfiguration cfg;

    public ConfigManager(JavaPlugin plugin) {
        this.cfg = plugin.getConfig();
    }

    // ---- AutoSell Chest ----
    public boolean isAutoSellEnabled() {
        return cfg.getBoolean("auto-sell.enabled", false);
    }
    public String getAutoSellName() {
        return cfg.getString("auto-sell.name", "AutoSell Chest");
    }
    public List<String> getAutoSellItems() {
        return cfg.getStringList("auto-sell.loot");
    }
    public double getAutoSellTaxPercent() {
        return cfg.getDouble("auto-sell.tax-percent", 0);
    }

    // ---- Crop Hopper ----
    public boolean isCropHopperEnabled() {
        return cfg.getBoolean("crop-hopper.enabled", false);
    }
    public List<String> getCropHopperLoot() {
        return cfg.getStringList("crop-hopper.loot");
    }
    public int getCropHopperInterval() {
        return cfg.getInt("crop-hopper.interval", 200);
    }
    public double getCropHopperTaxPercent() {
        return cfg.getDouble("crop-hopper.tax-percent", 0);
    }

    // ---- Mob Hopper ----
    public boolean isMobHopperEnabled() {
        return cfg.getBoolean("mob-hopper.enabled", false);
    }
    public List<String> getMobHopperLoot() {
        return cfg.getStringList("mob-hopper.loot");
    }
    public int getMobHopperInterval() {
        return cfg.getInt("mob-hopper.interval", 200);
    }
    public double getMobHopperTaxPercent() {
        return cfg.getDouble("mob-hopper.tax-percent", 0);
    }

    // ---- XP Hopper ----
    public boolean isXpHopperEnabled() {
        return cfg.getBoolean("xp-hopper.enabled", false);
    }
    public int getXpHopperInterval() {
        return cfg.getInt("xp-hopper.interval", 200);
    }
    public double getXpSellRate() {
        return cfg.getDouble("xp-hopper.sell-rate", 1);
    }
    public double getXpHopperTaxPercent() {
        return cfg.getDouble("xp-hopper.tax-percent", 0);
    }
}
