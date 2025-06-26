// File: src/main/java/com/example/customitems1/listeners/CropHopperListener.java
package com.example.customitems1.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.bgsoftware.wildstacker.api.WildStackerAPI;
import com.bgsoftware.wildstacker.api.objects.StackedEntity;
import com.example.customitems1.CropHopper;
import com.example.customitems1.CustomItems1;

public class CropHopperListener implements Listener {
    private final CustomItems1 plugin;

    public CropHopperListener(CustomItems1 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block b = event.getBlock();
        if (!(b.getBlockData() instanceof Ageable)) return;
        Ageable crop = (Ageable) b.getBlockData();
        if (crop.getAge() < crop.getMaximumAge()) return;

        Chunk chunk = b.getLocation().getChunk();
        Location origin = chunk.getBlock(0, 0, 0).getLocation();
        CropHopper hopper = plugin.getCropHoppers().get(origin);
        if (hopper == null) return;

        // Collect vanilla drops
        List<ItemStack> drops = new ArrayList<>(b.getDrops());
        b.breakNaturally();

        // Collect WildStacker drops around
        int radius = plugin.getCfg().getInt("wildstacker-radius", 5);
        Location loc = b.getLocation();
        for (var ent : loc.getWorld().getNearbyEntities(loc, radius, radius, radius)) {
            if (!(ent instanceof org.bukkit.entity.LivingEntity)) continue;
            var living = (org.bukkit.entity.LivingEntity) ent;
            StackedEntity se = WildStackerAPI.getStackedEntity(living);
            if (se == null) continue;

            int count = WildStackerAPI.getEntityAmount(living);
            drops.addAll(se.getDrops(count));
        }

        // Push into chest
        hopper.addAll(drops);
    }
}
