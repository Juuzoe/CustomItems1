package com.example.customitems1.listeners;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.example.customitems1.CustomItems1;
import com.example.customitems1.MobDropHopper;

public class MobDropHopperListener implements Listener {

    private final CustomItems1 plugin;
    private final FileConfiguration cfg;

    public MobDropHopperListener(CustomItems1 plugin) {
        this.plugin = plugin;
        this.cfg = plugin.getCfg();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        // 1) Find the "chunk‐center" location key
        Location chunkCenter = event.getEntity()
                .getLocation()
                .getChunk()
                .getBlock(0, 0, 0)
                .getLocation();

        // 2) Lookup our hopper stub
        MobDropHopper hopper = plugin.getMobHoppers().get(chunkCenter);
        if (hopper != null) {
            // TODO: implement actual drop‐collection logic here
        }
    }
}
