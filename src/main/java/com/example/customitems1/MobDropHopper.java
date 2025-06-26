// File: src/main/java/com/example/customitems1/MobDropHopper.java
package com.example.customitems1;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.ItemStack;

public class MobDropHopper {
    private final Hopper hopper;

    public MobDropHopper(BlockState state) {
        this.hopper = (Hopper) state;
    }

    public void addItem(ItemStack item) {
        hopper.getInventory().addItem(item);
    }

    public void addAll(List<ItemStack> items) {
        for (ItemStack i : items) addItem(i);
    }

    public static Location originOf(BlockState state) {
        Chunk c = state.getLocation().getChunk();
        return c.getBlock(0, 0, 0).getLocation();
    }
}
