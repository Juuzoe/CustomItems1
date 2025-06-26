// File: src/main/java/com/example/customitems1/listeners/HopperPlacementListener.java
package com.example.customitems1.listeners;

import com.example.customitems1.AutoSellChest;
import com.example.customitems1.CustomItems1;
import com.example.customitems1.MobDropHopper;
import com.example.customitems1.CropHopper;
import com.example.customitems1.XPHopper;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class HopperPlacementListener implements Listener {
    private final CustomItems1 plugin;

    public HopperPlacementListener(CustomItems1 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        BlockState state = event.getBlockPlaced().getState();
        Material mat = state.getType();

        if (mat == Material.HOPPER) {
            plugin.getMobHoppers().put(
                MobDropHopper.originOf(state),
                new MobDropHopper(state)
            );
            plugin.getCropHoppers().put(
                CropHopper.originOf(state),
                new CropHopper(state)
            );
            plugin.getXpHoppers().put(
                XPHopper.originOf(state),
                new XPHopper(state)
            );
        }
        else if (mat == Material.CHEST) {
            plugin.getAutoSellChests().put(
                AutoSellChest.originOf(state),
                new AutoSellChest(state)
            );
        }
    }
}
