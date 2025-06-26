// File: src/main/java/com/example/customitems1/listeners/AutoSellChestListener.java
package com.example.customitems1.listeners;

import com.example.customitems1.AutoSellChest;
import com.example.customitems1.CustomItems1;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.math.BigDecimal;

public class AutoSellChestListener implements Listener {
    private final CustomItems1 plugin;

    public AutoSellChestListener(CustomItems1 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDestroy(BlockBreakEvent e) {
        BlockState state = e.getBlock().getState();
        var loc = AutoSellChest.originOf(state);
        AutoSellChest chest = plugin.getAutoSellChests().remove(loc);
        if (chest == null) return;

        // Sell remaining contents immediately when broken
        for (var item : chest.getContents()) {
            if (!plugin.getCfg().getStringList("autosell.allowed_materials").contains("*")
                && !plugin.getCfg().getStringList("autosell.allowed_materials")
                            .contains(item.getType().name())) {
                continue;
            }
            BigDecimal price = ShopGuiPlusApi
                .getApi()
                .getSellPrice(item, plugin.getCfg().getString("autosell.shop-category"));

            // TODO: deposit `price * item.getAmount()` via SuperiorSkyblock2 API
        }
        chest.clearContents();
    }
}
