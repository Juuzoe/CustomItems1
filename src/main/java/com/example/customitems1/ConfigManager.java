package com.example.customitems1;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {
    private final FileConfiguration cfg;

    public ConfigManager(CustomItems1 plugin) {
        plugin.saveDefaultConfig();
        this.cfg = YamlConfiguration.loadConfiguration(
            new File(plugin.getDataFolder(), "config.yml")
        );
    }

    // —— Auto-Sell Chest ——
    public boolean isAutoSellEnabled() {
        return cfg.getBoolean("auto-sell.enabled", true);
    }
    public String getAutoSellName() {
        return cfg.getString("auto-sell.name", "AutoSell Chest");
    }
    public double getAutoSellTaxPercent() {
        return cfg.getDouble("auto-sell.tax-percent", 0.0);
    }
    public List<String> getAutoSellItems() {
        return cfg.getStringList("auto-sell.items");
    }

    // —— Crop Hopper ——
    public double getCropHopperTaxPercent() {
        return cfg.getDouble("crop-hopper.tax-percent", 0.0);
    }
    public List<String> getCropHopperItems() {
        return cfg.getStringList("crop-hopper.items");
    }

    // —— Mob Drop Hopper ——
    public double getMobHopperTaxPercent() {
        return cfg.getDouble("mob-hopper.tax-percent", 0.0);
    }
    public List<String> getMobHopperItems() {
        return cfg.getStringList("mob-hopper.items");
    }

    // —— XP Hopper ——
    public double getXpSellRate() {
        return cfg.getDouble("xp-hopper.sell-rate", 1.0);
    }
    public double getXpHopperTaxPercent() {
        return cfg.getDouble("xp-hopper.tax-percent", 0.0);
    }
}
