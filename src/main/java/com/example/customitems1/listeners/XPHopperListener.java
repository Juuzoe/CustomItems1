package com.example.customitems1.listeners;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import com.example.customitems1.CustomItems1;
import com.example.customitems1.XPHopper;

public class XPHopperListener implements Listener {

    private final CustomItems1 plugin;
    private final FileConfiguration cfg;

    public XPHopperListener(CustomItems1 plugin) {
        this.plugin = plugin;
        this.cfg = plugin.getCfg();
    }

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent event) {
        // 1) Find the chunk‐center key
        Location chunkCenter = event.getPlayer()
                .getLocation()
                .getChunk()
                .getBlock(0, 0, 0)
                .getLocation();

        // 2) Lookup our XP hopper stub
        XPHopper hopper = plugin.getXpHoppers().get(chunkCenter);
        if (hopper != null) {
            // TODO: implement XP capture logic here
            // e.g. absorb the XP into your hopper and zero it out:
            event.setAmount(0);
        }
    }
}
