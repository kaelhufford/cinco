package com.stigglespatch.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if(p.isOp()) {
                String  pL = p.getLocation().getWorld().toString();
                if (pL.equalsIgnoreCase("testdungeon")) {
                    p.sendMessage(ChatColor.GOLD+"Sending you to the CREATIVE Stiggles world.");
                    p.getLocation().setWorld(Bukkit.getWorld("world"));
                    p.getLocation().setX(0);
                    p.getLocation().setY(5);
                    p.getLocation().setZ(0);

                } else if (pL.equalsIgnoreCase("world")){
                    p.sendMessage(ChatColor.GOLD+"Sending you to the SEASON 5 world.");
                    p.getLocation().setWorld(Bukkit.getWorld("world_smp5"));
                    p.getLocation().setX(0);
                    p.getLocation().setY(5);
                    p.getLocation().setZ(0);

                } else if (pL.equalsIgnoreCase("world_smp5")){
                    p.sendMessage(ChatColor.GOLD+"Sending you to the DEBUG WORLD for Season 5.");
                    p.getLocation().setWorld(Bukkit.getWorld("testdungeon"));
                    p.getLocation().setX(0);
                    p.getLocation().setY(5);
                    p.getLocation().setZ(0);
                }
            } else {
                p.sendMessage(ChatColor.RED + "You are not allowed to use this command!");
            }
        } else {
            System.out.println("You may not use this command as anything but a player!");
        }
        return false;
    }
}
