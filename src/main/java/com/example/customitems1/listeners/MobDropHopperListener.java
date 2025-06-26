// File: src/main/java/com/example/customitems1/listeners/MobDropHopperListener.java
package com.example.customitems1.listeners;

import com.example.customitems1.CustomItems1;
import com.example.customitems1.MobDropHopper;
import com.bgsoftware.superiorskyblock.api.player.SuperiorPlayer;
import com.bgsoftware.wildstacker.api.WildStackerAPI;
import com.bgsoftware.wildstacker.api.wrappers.StackedEntity;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MobDropHopperListener implements Listener {
    private final CustomItems1 plugin;
    private final FileConfiguration cfg;

    public MobDropHopperListener(CustomItems1 plugin) {
        this.plugin = plugin;
        this.cfg = plugin.getCfg();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        // Determine chunk origin
        Location origin = event.getEntity()
                              .getLocation()
                              .getChunk()
                              .getBlock(0, 0, 0)
                              .getLocation();

        MobDropHopper hopper = plugin.getMobHoppers().get(origin);
        if (hopper == null) return;

        // 1) Gather vanilla drops
        List<ItemStack> drops = new ArrayList<>(event.getDrops());
        event.getDrops().clear();

        // 2) Include WildStacker stacked-entity drops
        if (WildStackerAPI.getAPI() != null) {
            int radius = cfg.getInt("wildstacker-radius", 5);
            for (Entity near : event.getEntity().getNearbyEntities(radius, radius, radius)) {
                if (WildStackerAPI.getAPI().isStackedEntity(near)) {
                    StackedEntity se = WildStackerAPI.getAPI().getStackedEntity(near);
                    drops.addAll(se.getDrops());
                }
            }
        }

        // 3) (Optional) Island check via SuperiorSkyblock2
        SuperiorPlayer sp = plugin.getServer()
                                  .getServicesManager()
                                  .getRegistration(SuperiorPlayer.class)
                                  .getProvider();
        // TODO: if needed, verify event.getEntity() is on the same island as the hopper owner

        // 4) Store all drops
        hopper.addAll(drops);
    }
}
