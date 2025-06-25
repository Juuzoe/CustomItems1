package com.example.customitems1.listeners;

import com.example.customitems1.CustomItems1;
import com.example.customitems1.ConfigManager;
import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.api.island.bank.IslandBank;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public class XPHopperListener extends BukkitRunnable {
    private final CustomItems1 plugin;
    private final ConfigManager cfg;

    public XPHopperListener(CustomItems1 plugin) {
        this.plugin = plugin;
        this.cfg = plugin.getCfg();
        runTaskTimer(plugin, 200, 200);
    }

    @Override
    public void run() {
        if (!cfg.isXpHopperEnabled()) return;
        double sellRate = cfg.getXpSellRate();
        double taxPct = cfg.getXpHopperTaxPercent();

        for (Map.Entry<Location, UUID> e : plugin.getXpHoppers().entrySet()) {
            Chunk chunk = e.getKey().getChunk();
            int totalXp = 0;
            for (Entity ent : chunk.getEntities()) {
                if (ent instanceof ExperienceOrb orb) {
                    totalXp += orb.getExperience();
                    orb.remove();
                }
            }
            if (totalXp == 0) continue;
            double gross = totalXp * sellRate;
            double tax = gross * taxPct / 100.0;
            double net = gross - tax;

            // give XP bottle to player’s island bank
            SuperiorPlayer sp = SuperiorSkyblockAPI.getPlayer(e.getValue());
            IslandBank bank = sp.getIsland().getBank();
            bank.depositMoney(sp, BigDecimal.valueOf(net));

            // drop an XP bottle at hopper location
            ItemStack bottle = new ItemStack(org.bukkit.Material.EXPERIENCE_BOTTLE);
            ItemMeta meta = bottle.getItemMeta();
            meta.setLore(
                java.util.List.of("§7Total XP: " + totalXp, "§7Net Sell: " + String.format("%.2f", net))
            );
            bottle.setItemMeta(meta);
            e.getKey().getWorld().dropItem(e.getKey(), bottle);
        }
    }
}
