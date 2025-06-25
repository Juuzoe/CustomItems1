package com.example.customitems1.listeners;

import com.example.customitems1.CustomItems1;
import com.example.customitems1.ConfigManager;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.player.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.api.island.bank.IslandBank;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

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
                double rate = cfg.getXpSellRate();
                double taxPct = cfg.getXpHopperTaxPercent();

                for (Map.Entry<Location, UUID> entry : plugin.getXpHoppers().entrySet()) {
                    Location loc = entry.getKey();
                    UUID ownerId = entry.getValue();

                    Chunk chunk = loc.getWorld().getChunkAt(loc);
                    double collectedXp = 0;

                    for (ExperienceOrb orb : chunk.getEntitiesByClass(ExperienceOrb.class)) {
                        collectedXp += orb.getExperience();
                        orb.remove();
                    }

                    if (collectedXp > 0) {
                        double gross = collectedXp * rate;
                        double tax   = gross * taxPct   / 100.0;
                        double net   = gross - tax;

                        SuperiorPlayer sp = SuperiorSkyblockAPI.getPlayer(ownerId);
                        IslandBank bank = sp.getIsland().getBank();
                        bank.depositMoney(sp, BigDecimal.valueOf(net));

                        Player owner = Bukkit.getPlayer(ownerId);
                        if (owner != null && owner.isOnline()) {
                            owner.sendMessage(
                              "§aXP‐Hopper sold for §e" + String.format("%.2f", net)
                            + " §7(after §c" + String.format("%.2f", tax) + " tax)"
                            );
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 200, 200);
    }
}
