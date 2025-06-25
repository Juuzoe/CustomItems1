package com.example.customitems1;

import org.bukkit.Location;

public class AutoSellChest {
    private final Location location;
    // TODO: integrate with ShopGUIPlus API and your tax-upgrade API

    public AutoSellChest(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
