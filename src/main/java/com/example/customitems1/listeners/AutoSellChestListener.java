package com.example.customitems1.listeners;

import com.example.customitems1.CustomItems1;
import com.example.customitems1.ConfigManager;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.player.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.api.island.bank.IslandBank;

import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AutoSellChestListener implements Listener {
    private final CustomItems1 plugin;
    private final ConfigManager cfg;

    public AutoSellChestListener(CustomItems1 plugin) {
        this.plugin = plugin;
        this.cfg = plugin.getCfg();

        // run every 10 seconds (200 ticks)
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!cfg.isAutoSellEnabled()) return;
                double taxPct = cfg.getAutoSellTaxPercent();
                List<String> sellItemKeys = cfg.getAutoSellItems();

                for (Map.Entry<Location, UUID> entry : plugin.getAutoSellChests().entrySet()) {
                    Location loc = entry.getKey();
                    UUID ownerId = entry.getValue();

                    if (!(loc.getBlock().getState() instanceof Chest chest)) continue;
                    Inventory inv = chest.getInventory();

                    double gross = 0;
                    for (ItemStack stack : inv.getContents()) {
                        if (stack == null) continue;
                        if (!sellItemKeys.contains(stack.getType().name())) continue;
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
                            + " §7(after §c" + String.format("%.2f", tax) + " tax)"
                            );
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 200, 200);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (!cfg.isAutoSellEnabled()) return;
        var meta = e.getItemInHand().getItemMeta();
        if (meta != null && meta.hasDisplayName()
         && meta.getDisplayName().equals(cfg.getAutoSellName())) {
            plugin.getAutoSellChests().put(
              e.getBlockPlaced().getLocation(),
              e.getPlayer().getUniqueId()
            );
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        plugin.getAutoSellChests().remove(e.getBlock().getLocation());
    }
}
