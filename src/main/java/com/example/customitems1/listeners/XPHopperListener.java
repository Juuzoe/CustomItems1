package com.example.customitems1.listeners;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;               // ← add this
import org.bukkit.scheduler.BukkitRunnable;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.bank.IslandBank;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.example.customitems1.ConfigManager;
import com.example.customitems1.CustomItems1;

public class XPHopperListener implements Listener {
    private final CustomItems1 plugin;
    private final ConfigManager cfg;

    public XPHopperListener(CustomItems1 plugin) {
        this.plugin = plugin;
        this.cfg = plugin.getCfg();

        new BukkitRunnable() {
            public void run() {
                double rate = cfg.getXpSellRate();
                double taxPct = cfg.getXpHopperTaxPercent();

                for (Map.Entry<Location, UUID> e : plugin.getXpHoppers().entrySet()) {
                    Location loc = e.getKey();
                    UUID ownerId = e.getValue();
                    Chunk chunk = loc.getChunk();

                    double grossXp = 0;
                    for (var ent : chunk.getEntities()) {
                        if (ent instanceof ExperienceOrb orb) {
                            grossXp += orb.getExperience();
                            orb.remove();
                        }
                    }

                    if (grossXp > 0) {
                        double grossMoney = grossXp * rate;
                        double tax = grossMoney * taxPct / 100.0;
                        double net = grossMoney - tax;

                        SuperiorPlayer sp = SuperiorSkyblockAPI.getPlayer(ownerId);
                        IslandBank bank = sp.getIsland().getIslandBank();
                        bank.depositMoney(sp, BigDecimal.valueOf(net));

                        Player owner = Bukkit.getPlayer(ownerId);
                        if (owner != null && owner.isOnline()) {
                            owner.sendMessage("§aXP Hopper sold §e" +
                              String.format("%.2f", net) +
                              " §7(after §c" + String.format("%.2f", tax) + " tax)"
                            );
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 200, 200);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        var meta = e.getItemInHand().getItemMeta();
        if (meta != null && meta.hasDisplayName()
         && meta.getDisplayName().contains("XP Hopper")) {
            plugin.getXpHoppers()
                  .put(e.getBlockPlaced().getLocation(), e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        plugin.getXpHoppers().remove(e.getBlock().getLocation());
    }
}
