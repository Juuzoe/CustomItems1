// File: src/main/java/com/example/customitems1/listeners/HopperPlacementListener.java
package com.example.customitems1.listeners;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.example.customitems1.AutoSellChest;
import com.example.customitems1.CropHopper;
import com.example.customitems1.CustomItems1;
import com.example.customitems1.MobDropHopper;
import com.example.customitems1.XPHopper;

public class HopperPlacementListener implements Listener {
    private final CustomItems1 plugin;
    private final NamespacedKey hopperKey;

    public HopperPlacementListener(CustomItems1 plugin) {
        this.plugin = plugin;
        this.hopperKey = new NamespacedKey(plugin, "custom_hopper_type");
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack placed = event.getItemInHand();
        if (placed == null || !placed.hasItemMeta()) return;

        ItemMeta meta = placed.getItemMeta();
        if (!meta.getPersistentDataContainer()
                 .has(hopperKey, PersistentDataType.STRING)) return;

        String type = meta.getPersistentDataContainer()
                          .get(hopperKey, PersistentDataType.STRING);
        Block block = event.getBlockPlaced();

        Location chunkOrigin = block.getLocation()
                                    .getChunk()
                                    .getBlock(0, 0, 0)
                                    .getLocation();
        Player player = event.getPlayer();

        switch (type) {
            case "mobdrop":
                plugin.getMobHoppers()
                      .put(chunkOrigin, new MobDropHopper(chunkOrigin));
                player.sendMessage("§aMobDropHopper placed!");
                break;
            case "crophopper":
                plugin.getCropHoppers()
                      .put(chunkOrigin, new CropHopper(chunkOrigin));
                player.sendMessage("§aCropHopper placed!");
                break;
            case "autosell":
                if (block.getState() instanceof Chest) {
                    plugin.getAutoSellChests()
                          .put(chunkOrigin, new AutoSellChest(chunkOrigin));
                    player.sendMessage("§aAutoSellChest placed!");
                } else {
                    player.sendMessage("§cPlace this on a chest!");
                }
                break;
            case "xphopper":
                plugin.getXpHoppers()
                      .put(chunkOrigin, new XPHopper(chunkOrigin));
                player.sendMessage("§aXPHopper placed!");
                break;
            default:
                player.sendMessage("§cUnknown hopper type: " + type);
        }
    }
}
