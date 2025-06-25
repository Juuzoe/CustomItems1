package com.example.customitems1.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.example.customitems1.CustomItems1;

public class GiveCommand implements CommandExecutor {

    private final CustomItems1 plugin;

    public GiveCommand(CustomItems1 plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may use this command.");
            return true;
        }

        Player player = (Player) sender;
        // TODO: parse args and give the appropriate hopper item
        player.sendMessage("§a[CustomItems1] This will give you a hopper stub once implemented.");
        return true;
    }
}
