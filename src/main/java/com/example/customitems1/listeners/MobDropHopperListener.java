package com.example.customitems1.listeners;

import com.example.customitems1.CustomItems1;
import com.example.customitems1.ConfigManager;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.player.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.api.island.bank.IslandBank;

import com.bgsoftware.wildstacker.api.WildStackerAPI;
import com.bgsoftware.wildstacker.api.objects.StackedItem;

import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MobDropHopperListener implements Listener {
    private final CustomItems1 plugin;
    private final ConfigManager cfg;

    public MobDropHopperListener(CustomItems1 plugin) {
        this.plugin = plugin;
        this.cfg = plugin.getCfg();

        new BukkitRunnable() {
            @Override
            public void run() {
                double taxPct = cfg.getMobHopperTaxPercent();
                List<String> validTypes = cfg.getMobHopperItems();

                for (Map.Entry<Location, UUID> entry : plugin.getMobHoppers().entrySet()) {
                    Location loc = entry.getKey();
                    UUID ownerId = entry.getValue();

                    Chunk chunk = loc.getWorld().getChunkAt(loc);
                    double gross = 0;

                    for (Item item : chunk.getEntitiesByClass(Item.class)) {
                        if (!validTypes.contains(item.getItemStack().getType().name())) continue;

                        StackedItem st = WildStackerAPI.getStackedItem(item);
                        int amt = (st != null ? st.getAmount() : item.getItemStack().getAmount());

                        double price = ShopGuiPlusApi.getItemStackPriceSell(null, item.getItemStack())
                                     * amt;
                        gross += price;
                        item.remove();
                    }

                    if (gross > 0) {
                        double tax = gross * taxPct / 100.0;
                        double net = gross - tax;

                        SuperiorPlayer sp = SuperiorSkyblockAPI.getPlayer(ownerId);
                        IslandBank bank = sp.getIsland().getBank();
                        bank.depositMoney(sp, BigDecimal.valueOf(net));

                        Player owner = plugin.getServer().getPlayer(ownerId);
                        if (owner != null && owner.isOnline()) {
                            owner.sendMessage(
                                "§aMob‐Hopper sold for §e" + String.format("%.2f", net)
                              + " §7(after §c" + String.format("%.2f", tax) + " tax)"
                            );
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 200, 200);
    }
}
