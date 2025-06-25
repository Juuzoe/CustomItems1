package com.example.customitems1.listeners;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.example.customitems1.CropHopper;
import com.example.customitems1.CustomItems1;

public class CropHopperListener implements Listener {

    private final CustomItems1 plugin;
    private final FileConfiguration cfg;

    public CropHopperListener(CustomItems1 plugin) {
        this.plugin = plugin;
        this.cfg = plugin.getCfg();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        // 1) Find the chunk‐center key
        Location chunkCenter = event.getBlock()
                .getLocation()
                .getChunk()
                .getBlock(0, 0, 0)
                .getLocation();

        // 2) Lookup our hopper stub
        CropHopper hopper = plugin.getCropHoppers().get(chunkCenter);
        if (hopper != null) {
            // TODO: implement crop‐collection logic here
        }
    }
}
