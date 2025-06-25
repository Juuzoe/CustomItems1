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
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public class CropHopperListener implements Listener {
    private final CustomItems1 plugin;
    private final ConfigManager cfg;

    public CropHopperListener(CustomItems1 plugin) {
        this.plugin = plugin;
        this.cfg = plugin.getCfg();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!cfg.isCropHopperEnabled()) return;

                Map<String, Double> sellItems = cfg.getCropHopperItems();

                for (Map.Entry<Location, UUID> e : plugin.getCropHoppers().entrySet()) {
                    Location loc = e.getKey();
                    UUID ownerId = e.getValue();
                    Chunk chunk = loc.getWorld().getChunkAt(loc);

                    for (Entity ent : chunk.getEntities()) {
                        if (!(ent instanceof Item drop)) continue;
                        ItemStack stack = drop.getItemStack();
                        String key = stack.getType().name();
                        if (!sellItems.containsKey(key)) continue;

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
        if (!cfg.isCropHopperEnabled()) return;
        if (e.getItemInHand().hasItemMeta()
         && cfg.getCropHopperName().equals(e.getItemInHand().getItemMeta().getDisplayName())) {
            plugin.getCropHoppers().put(e.getBlockPlaced().getLocation(), e.getPlayer().getUniqueId());
            e.getPlayer().sendMessage("§aRegistered a crop-hopper!");
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        plugin.getCropHoppers().remove(e.getBlock().getLocation());
    }
}
