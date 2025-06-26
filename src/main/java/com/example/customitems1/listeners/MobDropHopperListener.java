// File: src/main/java/com/example/customitems1/listeners/MobDropHopperListener.java
package com.example.customitems1.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.bgsoftware.wildstacker.api.WildStackerAPI;
import com.bgsoftware.wildstacker.api.objects.StackedEntity;
import com.example.customitems1.CustomItems1;
import com.example.customitems1.MobDropHopper;

public class MobDropHopperListener implements Listener {
    private final CustomItems1 plugin;

    public MobDropHopperListener(CustomItems1 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Location origin = event.getEntity()
                              .getLocation()
                              .getChunk()
                              .getBlock(0, 0, 0)
                              .getLocation();

        MobDropHopper hopper = plugin.getMobHoppers().get(origin);
        if (hopper == null) return;

        // 1) Vanilla drops
        List<ItemStack> drops = new ArrayList<>(event.getDrops());
        event.getDrops().clear();

        // 2) WildStacker stacked drops
        int radius = plugin.getCfg().getInt("wildstacker-radius", 5);
        for (var ent : event.getEntity().getNearbyEntities(radius, radius, radius)) {
            if (!(ent instanceof LivingEntity)) continue;
            LivingEntity living = (LivingEntity) ent;

            StackedEntity se = WildStackerAPI.getStackedEntity(living);
            if (se == null) continue;

            int count = WildStackerAPI.getEntityAmount(living);
            drops.addAll(se.getDrops(count));
        }

        hopper.addAll(drops);
    }
}
