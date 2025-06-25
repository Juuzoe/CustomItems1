package com.example.customitems1;

import org.bukkit.Location;

public class XPHopper {
    private final Location location;
    // TODO: accumulate XP and store in bottles

    public XPHopper(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
