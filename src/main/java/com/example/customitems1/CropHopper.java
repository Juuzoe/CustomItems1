// File: src/main/java/com/example/customitems1/CropHopper.java
package com.example.customitems1;

import java.util.List;

import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

public class CropHopper {
    private final Chest chest;

    public CropHopper(BlockState state) {
        this.chest = (Chest) state;
    }

    public void addItem(ItemStack item) {
        chest.getBlockInventory().addItem(item);
    }

    /** Batch-add a list of ItemStacks */
    public void addAll(List<ItemStack> items) {
        for (ItemStack item : items) {
            addItem(item);
        }
    }

    /** Use chunk-origin as key */
    public static org.bukkit.Location originOf(BlockState state) {
        return state.getLocation()
                    .getChunk()
                    .getBlock(0, 0, 0)
                    .getLocation();
    }
}
