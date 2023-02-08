package com.stigglespatch.main.Dungeon;

import com.stigglespatch.main.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DungeonCommand implements CommandExecutor{
    private Main main;
    DungeonStartCommand dSC = new DungeonStartCommand();

    public DungeonCommand(Main main) {this.main = main;}

    /**

Remake dungeon command to:
/dungeon world - send the admin to the dungeon world || ADMINS ONLY
/dungeon start <private/public> - creates a new party for the player and broadcasts the message above, if set to private, only they can invite people to do the dungeon, if set to public, anyone can join them
/dungeon invite <player> - sends an invitation to a player to join the dungeon (IF NOT ALREADY IN IT)
/dungeon join <player> - checks if the mentioned player is in a dungeon or not, if they are, and if the dungeon is not full they will join || Also allows invited players to join an dungeon they were invited to.
/dungeon stats
/dungeon stage
/dungeon party - lists the dungeon raid party
/dungeon level
ADD MORE

 extra stuff

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

*/

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
/*
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("level")) {
                    if (dSC.getPlayersList().contains(p)) {
                        p.sendMessage(ChatColor.GREEN + "You are currently on level " + main.getRoomNumber() + ".");
                    } else {
                        p.sendMessage(ChatColor.RED + "You must be in a dungeon to use this command!");
                    }
                } else if (args[0].equalsIgnoreCase("party")) {

                } else if (args[0].equalsIgnoreCase("stage")) {

                } else if (args[0].equalsIgnoreCase("start")) {

                } else if (args[0].equalsIgnoreCase("stats")) {

                } else if (args[0].equalsIgnoreCase("world")) {
                    if (p.isOp()) {
                        p.teleport(Bukkit.getWorld("testdungeon").getSpawnLocation());
                    } else {
                        p.sendMessage(ChatColor.RED + "You must be operator if you wish to use this command!");
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "You did not this command right! Please double check to see if you misused anything!");
                }

            } else if (args.length == 2) {

                if (args[0].equalsIgnoreCase("invite")) {

                } else if (args[0].equalsIgnoreCase("join")) {

                } else {
                    p.sendMessage(ChatColor.RED + "You did not this command right! Please double check to see if you misused anything!");
                }

            } else {
                p.sendMessage(ChatColor.RED + "You did not this command right! Please double check to see if you misused anything!");
            }
        } else {
            System.out.println("You may not use this command in console!");
        }

        return false;*/
        return false;
    }
}
