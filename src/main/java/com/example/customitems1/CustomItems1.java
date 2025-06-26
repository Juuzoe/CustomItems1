// File: src/main/java/com/example/customitems1/CustomItems1.java
package com.example.customitems1;

import com.example.customitems1.commands.GiveCommand;
import com.example.customitems1.listeners.CropHopperListener;
import com.example.customitems1.listeners.HopperPlacementListener;
import com.example.customitems1.listeners.MobDropHopperListener;
import com.example.customitems1.listeners.AutoSellChestListener;
import com.example.customitems1.listeners.XPHopperListener;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.util.*;

public class CustomItems1 extends JavaPlugin {
    private FileConfiguration cfg;

    private final Map<Location, MobDropHopper> mobHoppers = new HashMap<>();
    private final Map<Location, CropHopper> cropHoppers = new HashMap<>();
    private final Map<Location, AutoSellChest> autoSellChests = new HashMap<>();
    private final Map<Location, XPHopper> xpHoppers = new HashMap<>();

    private Set<Material> autoAllowed;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        cfg = getConfig();

        // Build autosell allowed set
        List<String> allowedList = cfg.getStringList("autosell.allowed_materials");
        if (allowedList.contains("*")) {
            autoAllowed = null;
        } else {
            autoAllowed = new HashSet<>();
            for (String name : allowedList) {
                Material m = Material.getMaterial(name);
                if (m != null) autoAllowed.add(m);
            }
        }

        // Register command
        getCommand("givehopper").setExecutor(new GiveCommand(this));

        // Register listeners
        var pm = getServer().getPluginManager();
        pm.registerEvents(new HopperPlacementListener(this), this);
        pm.registerEvents(new MobDropHopperListener(this), this);
        pm.registerEvents(new CropHopperListener(this), this);
        pm.registerEvents(new AutoSellChestListener(this), this);
        pm.registerEvents(new XPHopperListener(this), this);

        // Schedule auto-sell
        int interval = cfg.getInt("autosell.interval", 10);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (AutoSellChest chest : autoSellChests.values()) {
                    List<ItemStack> contents = chest.getContents();
                    if (contents.isEmpty()) continue;

                    BigDecimal total = BigDecimal.ZERO;
                    for (ItemStack item : contents) {
                        Material type = item.getType();
                        if (autoAllowed != null && !autoAllowed.contains(type)) continue;

                        BigDecimal price = ShopGuiPlusApi
                            .getApi()
                            .getSellPrice(item, cfg.getString("autosell.shop-category"));

                        total = total.add(price.multiply(BigDecimal.valueOf(item.getAmount())));
                    }
                    if (total.compareTo(BigDecimal.ZERO) <= 0) continue;

                    BigDecimal net = total.multiply(
                        BigDecimal.ONE.subtract(BigDecimal.valueOf(cfg.getDouble("autosell.tax")))
                    );

                    // TODO: deposit `net` via SuperiorSkyblock2 API

                    // Remove only the sold items
                    for (ItemStack item : contents) {
                        Material type = item.getType();
                        if (autoAllowed == null || autoAllowed.contains(type)) {
                            chest.removeItemsOfType(type);
                        }
                    }
                }
            }
        }.runTaskTimer(this, interval * 20L, interval * 20L);
    }

    public FileConfiguration getCfg() { return cfg; }
    public Map<Location, MobDropHopper> getMobHoppers() { return mobHoppers; }
    public Map<Location, CropHopper> getCropHoppers() { return cropHoppers; }
    public Map<Location, AutoSellChest> getAutoSellChests() { return autoSellChests; }
    public Map<Location, XPHopper> getXpHoppers() { return xpHoppers; }
}
