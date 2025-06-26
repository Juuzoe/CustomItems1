// File: src/main/java/com/example/customitems1/AutoSellChest.java
package com.example.customitems1;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

public class AutoSellChest {
    private final Chest chestBlock;

    public AutoSellChest(BlockState state) {
        this.chestBlock = (Chest) state;
    }

    /** Returns a *copy* of the current chest contents */
    public List<ItemStack> getContents() {
        ItemStack[] contents = chestBlock.getBlockInventory().getContents();
        List<ItemStack> list = new ArrayList<>();
        for (ItemStack item : contents) {
            if (item != null && item.getAmount() > 0) {
                list.add(item.clone());
            }
        }
        return list;
    }

    /** Removes *all* stacks of the given material from the chest */
    public void removeItemsOfType(org.bukkit.Material type) {
        Chest invChest = chestBlock;
        ItemStack[] contents = invChest.getBlockInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null && contents[i].getType() == type) {
                contents[i] = null;
            }
        }
        invChest.getBlockInventory().setContents(contents);
    }

    /** Clears *all* items from the chest */
    public void clearContents() {
        chestBlock.getBlockInventory().clear();
    }

    /** Helper to get the chest’s chunk‐origin location key */
    public static Location originOf(BlockState state) {
        return state.getLocation().getChunk().getBlock(0, 0, 0).getLocation();
    }
}
