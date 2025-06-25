package com.example.customitems1;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {

    private final FileConfiguration cfg;

    public ConfigManager(File dataFolder) {
        File file = new File(dataFolder, "config.yml");
        if (!file.exists()) {
            dataFolder.mkdirs();
            // assume plugin savesResource("config.yml", false) elsewhere
        }
        this.cfg = YamlConfiguration.loadConfiguration(file);
    }

    public boolean isAutoSellEnabled() {
        return cfg.getBoolean("autosell.enabled", true);
    }
    public String getAutoSellName() {
        return cfg.getString("autosell.name", "§eAutoSell Chest");
    }
    @SuppressWarnings("unchecked")
    public Map<String,Object> getAutoSellItems() {
        return (Map<String,Object>)cfg.getConfigurationSection("autosell.items").getValues(false);
    }
    public double getAutoSellTaxPercent() {
        return cfg.getDouble("autosell.tax-percent", 0.0);
    }

    public boolean isCropHopperEnabled() {
        return cfg.getBoolean("hoppers.crop.enabled", true);
    }
    public String getCropHopperName() {
        return cfg.getString("hoppers.crop.name", "§aCrop Hopper");
    }
    public List<String> getCropHopperItems() {
        return cfg.getStringList("hoppers.crop.loot");
    }
    public double getCropHopperTaxPercent() {
        return cfg.getDouble("hoppers.crop.tax-percent", 0.0);
    }

    public boolean isMobHopperEnabled() {
        return cfg.getBoolean("hoppers.mob.enabled", true);
    }
    public String getMobHopperName() {
        return cfg.getString("hoppers.mob.name", "§cMob Drop Hopper");
    }
    public List<String> getMobHopperItems() {
        return cfg.getStringList("hoppers.mob.loot");
    }
    public double getMobHopperTaxPercent() {
        return cfg.getDouble("hoppers.mob.tax-percent", 0.0);
    }

    public boolean isXpHopperEnabled() {
        return cfg.getBoolean("hoppers.xp.enabled", true);
    }
    public String getXpHopperName() {
        return cfg.getString("hoppers.xp.name", "§bXP Hopper");
    }
    public double getXpSellRate() {
        return cfg.getDouble("hoppers.xp.sell-rate", 1.0);
    }
    public double getXpHopperTaxPercent() {
        return cfg.getDouble("hoppers.xp.tax-percent", 0.0);
    }
}
