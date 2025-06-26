// File: src/main/java/com/example/customitems1/MobDropHopper.java
package com.example.customitems1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class MobDropHopper {

    private final Location location;
    private final List<ItemStack> storedDrops = new ArrayList<>();

    public MobDropHopper(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void addItem(ItemStack drop) {
        storedDrops.add(drop);
    }

    public void addAll(List<ItemStack> drops) {
        storedDrops.addAll(drops);
    }

    public List<ItemStack> getStoredDrops() {
        return Collections.unmodifiableList(storedDrops);
    }

    public void clear() {
        storedDrops.clear();
    }
}
