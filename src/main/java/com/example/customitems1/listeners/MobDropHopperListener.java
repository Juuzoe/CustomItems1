package com.example.customitems1.listeners;

import com.example.customitems1.ConfigManager;
import com.example.customitems1.CustomItems1;
import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.player.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.api.island.bank.IslandBank;
import me.botsko.wildstacker.api.WildStackerAPI;
import me.botsko.wildstacker.api.entities.StackedEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MobDropHopperListener implements Listener {
    private final CustomItems1 plugin;
    private final ConfigManager cfg;

    public MobDropHopperListener(CustomItems1 plugin) {
        this.plugin = plugin;
        this.cfg    = plugin.getCfg();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!cfg.isMobHopperEnabled()) return;
                double taxPct      = cfg.getMobHopperTaxPercent();
                List<String> loot  = cfg.getMobHopperLoot();

                for (Map.Entry<Location, UUID> entry : plugin.getMobHoppers().entrySet()) {
                    Location loc     = entry.getKey();
                    UUID     ownerId = entry.getValue();

                    var chunk = loc.getChunk();
                    double gross = 0;

                    for (Item ent : chunk.getEntitiesByClass(Item.class)) {
                        if (!loot.contains(ent.getItemStack().getType().name()))
                            continue;
                        if (WildStackerAPI.isStacked(ent)) {
                            StackedEntity st = WildStackerAPI.getStackedEntity(ent);
                            gross += ent.getItemStack().getAmount() * st.getAmount();
                            ent.remove();
                        } else {
                            gross += ent.getItemStack().getAmount();
                            ent.remove();
                        }
                    }

                    if (gross > 0) {
                        double tax = gross * taxPct / 100.0;
                        double net = gross - tax;

                        SuperiorPlayer sp = SuperiorSkyblockAPI.getPlayer(ownerId);
                        IslandBank bank   = sp.getIsland().getBank();
                        bank.depositMoney(sp, net);

                        Player owner = Bukkit.getPlayer(ownerId);
                        if (owner != null && owner.isOnline()) {
                            owner.sendMessage(String.format(
                                "§aMob-hopper sold §e%.2f §7(after §c%.2f tax)",
                                net, tax
                            ));
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, cfg.getMobHopperInterval(), cfg.getMobHopperInterval());
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (!cfg.isMobHopperEnabled()) return;
        plugin.getMobHoppers().put(e.getBlockPlaced().getLocation(), e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        plugin.getMobHoppers().remove(e.getBlock().getLocation());
    }
}
