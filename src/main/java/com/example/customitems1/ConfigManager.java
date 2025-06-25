package com.example.customitems1;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final FileConfiguration cfg;

    public ConfigManager(CustomItems1 plugin) {
        plugin.saveDefaultConfig();
        this.cfg = plugin.getConfig();
    }

    // ── AutoSellChest ───────────────────────────────────────────────────────────

    public boolean isAutoSellEnabled() {
        return cfg.getBoolean("autosell.enabled", true);
    }

    public double getAutoSellTaxPercent() {
        return cfg.getDouble("autosell.tax-percent", 0.0);
    }

    public Map<String, Double> getAutoSellItems() {
        return sectionToDoubleMap("autosell.items");
    }

    public String getAutoSellName() {
        return cfg.getString("autosell.name", "&6AutoSell Chest");
    }

    // ── MobDropHopper ───────────────────────────────────────────────────────────

    public boolean isMobHopperEnabled() {
        return cfg.getBoolean("mobhopper.enabled", true);
    }

    public double getMobHopperTaxPercent() {
        return cfg.getDouble("mobhopper.tax-percent", 0.0);
    }

    public Map<String, Double> getMobHopperItems() {
        return sectionToDoubleMap("mobhopper.items");
    }

    public String getMobHopperName() {
        return cfg.getString("mobhopper.name", "&6Mob Hopper");
    }

    // ── CropHopper ──────────────────────────────────────────────────────────────

    public boolean isCropHopperEnabled() {
        return cfg.getBoolean("crophopper.enabled", true);
    }

    public double getCropHopperTaxPercent() {
        return cfg.getDouble("crophopper.tax-percent", 0.0);
    }

    public Map<String, Double> getCropHopperItems() {
        return sectionToDoubleMap("crophopper.items");
    }

    public String getCropHopperName() {
        return cfg.getString("crophopper.name", "&6Crop Hopper");
    }

    // ── XPHopper ────────────────────────────────────────────────────────────────

    public boolean isXpHopperEnabled() {
        return cfg.getBoolean("xphopper.enabled", true);
    }

    public double getXpHopperTaxPercent() {
        return cfg.getDouble("xphopper.tax-percent", 0.0);
    }

    public double getXpSellRate() {
        return cfg.getDouble("xphopper.sell-rate", 1.0);
    }

    public String getXpHopperName() {
        return cfg.getString("xphopper.name", "&6XP Hopper");
    }

    // ── Utility ────────────────────────────────────────────────────────────────

    private Map<String, Double> sectionToDoubleMap(String path) {
        ConfigurationSection sec = cfg.getConfigurationSection(path);
        Map<String, Double> map = new HashMap<>();
        if (sec != null) {
            for (String key : sec.getKeys(false)) {
                map.put(key, sec.getDouble(key));
            }
        }
        return map;
    }
}
