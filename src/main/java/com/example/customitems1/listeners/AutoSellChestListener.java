package com.example.customitems1.listeners;

import com.example.customitems1.CustomItems1;
import com.example.customitems1.ConfigManager;

// SuperiorSkyblock2 API
import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.bank.IslandBank;

// ShopGUIPlus
import net.brcdev.shopgui.ShopGuiPlusApi;

// Bukkit
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

// Java
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;


public class AutoSellChestListener implements Listener {
    private final CustomItems1 plugin;
    private final ConfigManager cfg;

    public AutoSellChestListener(CustomItems1 plugin) {
        this.plugin = plugin;
        this.cfg = plugin.getCfg();

        // every 10s (200 ticks)
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!cfg.isAutoSellEnabled()) return;

                double taxPct = cfg.getAutoSellTaxPercent();
                Map<String, Double> sellItems = cfg.getAutoSellItems();

                for (Map.Entry<Location, UUID> e : plugin.getAutoSellChests().entrySet()) {
                    Location loc = e.getKey();
                    UUID ownerId = e.getValue();

                    if (!(loc.getBlock().getState() instanceof Chest chest)) continue;
                    Inventory inv = chest.getInventory();

                    double gross = 0;
                    for (ItemStack stack : inv.getContents()) {
                        if (stack == null) continue;
                        String key = stack.getType().name();
                        if (!sellItems.containsKey(key)) continue;
                        try {
                            double price = ShopGuiPlusApi.getItemStackPriceSell(null, stack)
                                         * stack.getAmount();
                            gross += price;
                            inv.remove(stack);
                        } catch (Exception ignored) {}
                    }

                    if (gross > 0) {
                        double tax = gross * taxPct / 100.0;
                        double net = gross - tax;
                        SuperiorPlayer sp = SuperiorSkyblockAPI.getPlayer(ownerId);
                        IslandBank bank = sp.getIsland().getBank();
                        bank.depositMoney(sp, BigDecimal.valueOf(net));

                        Player owner = Bukkit.getPlayer(ownerId);
                        if (owner != null && owner.isOnline()) {
                            owner.sendMessage(
                              "§aAuto-sold §e" + String.format("%.2f", net)
                            + " §7(after §c" + String.format("%.2f", tax) + " tax)");
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 200L, 200L);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (!cfg.isAutoSellEnabled()) return;
        if (e.getItemInHand().hasItemMeta()
         && cfg.getAutoSellName().equals(e.getItemInHand().getItemMeta().getDisplayName())) {
            plugin.getAutoSellChests().put(e.getBlockPlaced().getLocation(), e.getPlayer().getUniqueId());
            e.getPlayer().sendMessage("§aRegistered an auto-sell chest!");
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        plugin.getAutoSellChests().remove(e.getBlock().getLocation());
    }
}
