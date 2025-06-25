package com.example.customitems1.listeners;

import com.example.customitems1.CustomItems1;
import com.example.customitems1.ConfigManager;
import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.player.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.api.island.bank.IslandBank;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public class XPHopperListener implements Listener {
    private final CustomItems1 plugin;
    private final ConfigManager cfg;

    public XPHopperListener(CustomItems1 plugin) {
        this.plugin = plugin;
        this.cfg = plugin.getCfg();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!cfg.isXpHopperEnabled()) return;

                double rate = cfg.getXpSellRate();
                double taxPct = cfg.getXpHopperTaxPercent();

                for (Map.Entry<Location, UUID> e : plugin.getXpHoppers().entrySet()) {
                    Location loc = e.getKey();
                    UUID ownerId = e.getValue();
                    Chunk chunk = loc.getWorld().getChunkAt(loc);

                    for (Entity ent : chunk.getEntities()) {
                        if (!(ent instanceof ExperienceOrb orb)) continue;
                        int xp = orb.getExperience();
                        double gross = xp * rate;
                        double tax = gross * taxPct / 100.0;
                        double net = gross - tax;

                        SuperiorPlayer sp = SuperiorSkyblockAPI.getPlayer(ownerId);
                        IslandBank bank = sp.getIsland().getBank();
                        bank.depositMoney(sp, BigDecimal.valueOf(net));

                        orb.remove();
                    }
                }
            }
        }.runTaskTimer(plugin, 200L, 200L);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (!cfg.isXpHopperEnabled()) return;
        if (e.getItemInHand().hasItemMeta()
         && cfg.getXpHopperName().equals(e.getItemInHand().getItemMeta().getDisplayName())) {
            plugin.getXpHoppers().put(e.getBlockPlaced().getLocation(), e.getPlayer().getUniqueId());
            e.getPlayer().sendMessage("§aRegistered an XP-hopper!");
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        plugin.getXpHoppers().remove(e.getBlock().getLocation());
    }
}
