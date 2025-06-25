package com.example.customitems1.listeners;

import com.example.customitems1.CustomItems1;
import com.example.customitems1.ConfigManager;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;

import com.bgsoftware.wildstacker.api.WildStackerAPI;
import com.bgsoftware.wildstacker.api.objects.StackedItem;

import net.brcdev.shopgui.ShopGuiPlusApi;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public class MobDropHopperListener implements Listener {
    private final CustomItems1 plugin;
    private final ConfigManager cfg;

    public MobDropHopperListener(CustomItems1 plugin) {
        this.plugin = plugin;
        this.cfg = plugin.getCfg();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!cfg.isMobHopperEnabled()) return;
                double taxPct    = cfg.getMobHopperTaxPercent();
                Map<String, Object> sellItems = cfg.getMobHopperItems();

                for (Map.Entry<Location, UUID> e : plugin.getMobHoppers().entrySet()) {
                    Location loc  = e.getKey();
                    UUID ownerId  = e.getValue();
                    Chunk chunk   = loc.getChunk();

                    for (Item drop : chunk.getEntitiesByClass(Item.class)) {
                        if (!WildStackerAPI.isStacked(drop)) continue;
                        StackedItem st    = WildStackerAPI.getStackedItem(drop);
                        ItemStack base    = drop.getItemStack();
                        String mat        = base.getType().name();
                        if (!sellItems.containsKey(mat)) continue;

                        int count         = st.getAmount();
                        double unitPrice  = ShopGuiPlusApi.getItemStackPriceSell(null, base);
                        double gross      = count * unitPrice;
                        double tax        = gross * taxPct / 100.0;
                        double net        = gross - tax;

                        SuperiorPlayer sp = SuperiorSkyblockAPI.getPlayer(ownerId);
                        sp.getIsland().getBank()
                          .depositMoney(sp, BigDecimal.valueOf(net));

                        drop.remove();
                    }
                }
            }
        }.runTaskTimer(plugin, 200, 200);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (!cfg.isMobHopperEnabled()) return;
        var meta = e.getItemInHand().getItemMeta();
        if (meta != null
         && meta.hasDisplayName()
         && meta.getDisplayName().equals(cfg.getMobHopperName())) {
            plugin.getMobHoppers()
                  .put(e.getBlockPlaced().getLocation(),
                       e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        plugin.getMobHoppers().remove(e.getBlock().getLocation());
    }
}
