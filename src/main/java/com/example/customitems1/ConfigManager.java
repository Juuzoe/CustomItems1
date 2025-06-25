package com.example.customitems1;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final FileConfiguration cfg;

    public ConfigManager(CustomItems1 plugin) {
        this.cfg = plugin.getConfig();

        // ensure defaults
        cfg.addDefault("autosell.enabled", true);
        cfg.addDefault("autosell.items", List.of("DIAMOND", "IRON_INGOT"));
        cfg.addDefault("autosell.tax-percent", 0.0);
        cfg.addDefault("crophopper.items", List.of("WHEAT", "CARROT"));
        cfg.addDefault("crophopper.tax-percent", 0.0);
        cfg.addDefault("mobhopper.items", List.of("BONE", "STRING"));
        cfg.addDefault("mobhopper.tax-percent", 0.0);
        cfg.addDefault("xphopper.sell-rate", 1.0);
        cfg.addDefault("xphopper.tax-percent", 0.0);

        cfg.options().copyDefaults(true);
        plugin.saveConfig();
    }

    public boolean isAutoSellEnabled() {
        return cfg.getBoolean("autosell.enabled");
    }
    public List<String> getAutoSellItems() {
        return cfg.getStringList("autosell.items");
    }
    public double getAutoSellTaxPercent() {
        return cfg.getDouble("autosell.tax-percent");
    }

    public List<String> getCropHopperItems() {
        return cfg.getStringList("crophopper.items");
    }
    public double getCropHopperTaxPercent() {
        return cfg.getDouble("crophopper.tax-percent");
    }

    public List<String> getMobHopperItems() {
        return cfg.getStringList("mobhopper.items");
    }
    public double getMobHopperTaxPercent() {
        return cfg.getDouble("mobhopper.tax-percent");
    }

    public double getXpSellRate() {
        return cfg.getDouble("xphopper.sell-rate");
    }
    public double getXpHopperTaxPercent() {
        return cfg.getDouble("xphopper.tax-percent");
    }
}
