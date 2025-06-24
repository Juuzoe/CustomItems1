package com.example.customitems1.listeners;

import com.example.customitems1.ConfigManager;
import com.example.customitems1.CustomItems1;
import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.player.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.api.island.bank.IslandBank;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public class XPHopperListener implements Listener {
    private final CustomItems1 plugin;
    private final ConfigManager cfg;

    public XPHopperListener(CustomItems1 plugin) {
        this.plugin = plugin;
        this.cfg    = plugin.getCfg();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!cfg.isXpHopperEnabled()) return;
                double sellRate = cfg.getXpSellRate();
                double taxPct   = cfg.getXpHopperTaxPercent();

                for (Map.Entry<Location, UUID> entry : plugin.getXpHoppers().entrySet()) {
                    Location loc     = entry.getKey();
                    UUID     ownerId = entry.getValue();

                    var chunk = loc.getChunk();
                    double totalXp = 0;

                    for (ExperienceOrb orb : chunk.getEntitiesByClass(ExperienceOrb.class)) {
                        totalXp += orb.getExperience();
                        orb.remove();
                    }

                    if (totalXp > 0) {
                        double gross = totalXp * sellRate;
                        double tax   = gross * taxPct / 100.0;
                        double net   = gross - tax;

                        SuperiorPlayer sp = SuperiorSkyblockAPI.getPlayer(ownerId);
                        IslandBank bank   = sp.getIsland().getBank();
                        bank.depositMoney(sp, net);

                        Player owner = Bukkit.getPlayer(ownerId);
                        if (owner != null && owner.isOnline()) {
                            owner.sendMessage(String.format(
                                "§aXP-hopper sold §e%.2f §7(after §c%.2f tax)",
                                net, tax
                            ));
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, cfg.getXpHopperInterval(), cfg.getXpHopperInterval());
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (!cfg.isXpHopperEnabled()) return;
        plugin.getXpHoppers().put(e.getBlockPlaced().getLocation(), e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        plugin.getXpHoppers().remove(e.getBlock().getLocation());
    }
}
