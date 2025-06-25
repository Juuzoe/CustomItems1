package com.example.customitems1.listeners;

import com.example.customitems1.CustomItems1;
import com.example.customitems1.ConfigManager;
import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.api.island.bank.IslandBank;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public class CropHopperListener extends BukkitRunnable {
    private final CustomItems1 plugin;
    private final ConfigManager cfg;

    public CropHopperListener(CustomItems1 plugin) {
        this.plugin = plugin;
        this.cfg = plugin.getCfg();
        runTaskTimer(plugin, 200, 200);
    }

    @Override
    public void run() {
        if (!cfg.isCropHopperEnabled()) return;
        double taxPct = cfg.getCropHopperTaxPercent();
        var valid = cfg.getCropHopperItems();

        for (Map.Entry<Location, UUID> e : plugin.getCropHoppers().entrySet()) {
            Chunk chunk = e.getKey().getChunk();
            double gross = 0;
            for (Entity ent : chunk.getEntities()) {
                if (!(ent instanceof Item drop)) continue;
                ItemStack stack = drop.getItemStack();
                if (!valid.contains(stack.getType().name())) continue;
                double price = ShopGuiPlusApi.getItemStackPriceSell(null, stack) * stack.getAmount();
                gross += price;
                drop.remove();
            }
            if (gross <= 0) continue;
            double tax = gross * taxPct / 100.0;
            double net = gross - tax;
            SuperiorPlayer sp = SuperiorSkyblockAPI.getPlayer(e.getValue());
            IslandBank bank = sp.getIsland().getBank();
            bank.depositMoney(sp, BigDecimal.valueOf(net));
        }
    }
}
