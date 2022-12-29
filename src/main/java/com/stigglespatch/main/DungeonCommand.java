package com.stigglespatch.main;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DungeonCommand implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if(p.isOp()) {
                ((Player) sender).teleport(Bukkit.getWorld("testdungeon").getSpawnLocation());
            } else {
                p.sendMessage(ChatColor.RED + "You are not allowed to use this command!");
            }
        } else {
            System.out.println("You may not use this command as anything but a player!");
        }
        return false;
    }
}
