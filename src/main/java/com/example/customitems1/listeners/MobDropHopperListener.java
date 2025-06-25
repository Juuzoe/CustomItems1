package com.example.customitems1.listeners;

import com.example.customitems1.CustomItems1;
import com.example.customitems1.ConfigManager;
import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.player.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.api.island.bank.IslandBank;
import com.bgsoftware.wildstacker.api.WildStackerAPI;
import com.bgsoftware.wildstacker.api.wrappers.StackedEntity;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public class MobDropHopperListener implements Listener {
    private final CustomItems1 plugin;
    private final ConfigManager cfg;

    public MobDropHopperListener(CustomItems1 plugin) {
        this.plugin = plugin;
        this.cfg = plugin.getCfg();

        // every 10s
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!cfg.isMobHopperEnabled()) return;

                Map<String, Double> sellItems = cfg.getMobHopperItems();

                for (Map.Entry<Location, UUID> e : plugin.getMobHoppers().entrySet()) {
                    Location loc = e.getKey();
                    UUID ownerId = e.getValue();
                    Chunk chunk = loc.getWorld().getChunkAt(loc);

                    for (Entity ent : chunk.getEntities()) {
                        if (!(ent instanceof Item drop)) continue;
                        ItemStack stack = drop.getItemStack();
                        String key = stack.getType().name();
                        if (!sellItems.containsKey(key)) continue;

                        // handle stacked via WildStacker
                        if (WildStackerAPI.isStackedEntity(ent)) {
                            StackedEntity st = WildStackerAPI.getStackedEntity(ent);
                            stack.setAmount(st.getAmount());
                        }

                        SuperiorPlayer sp = SuperiorSkyblockAPI.getPlayer(ownerId);
                        IslandBank bank = sp.getIsland().getBank();
                        bank.depositItem(sp, stack);

                        drop.remove();
                    }
                }
            }
        }.runTaskTimer(plugin, 200L, 200L);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (!cfg.isMobHopperEnabled()) return;
        if (e.getItemInHand().hasItemMeta()
         && cfg.getMobHopperName().equals(e.getItemInHand().getItemMeta().getDisplayName())) {
            plugin.getMobHoppers().put(e.getBlockPlaced().getLocation(), e.getPlayer().getUniqueId());
            e.getPlayer().sendMessage("§aRegistered a mob-drop hopper!");
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        plugin.getMobHoppers().remove(e.getBlock().getLocation());
    }
}
