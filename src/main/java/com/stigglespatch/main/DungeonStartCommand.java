package com.stigglespatch.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class DungeonStartCommand implements CommandExecutor, Listener {

    public static ArrayList players = new ArrayList<Player>();
    public static ArrayList alivePlayers = new ArrayList<Player>();
    public ArrayList<Player> getPlayersList(){
        return players;
    }
    public ArrayList<Player> getAlivePlayers(){
        return alivePlayers;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
                if(sender.isOp()) {
                    if (!players.contains(p)) {
                        if ((players.size()) <= 3) {//3
                            p.sendMessage(ChatColor.GREEN + "You are being sent to the dungeon! Prepare yourself! (" + Math.addExact(1, players.size()) + "/4 Players)");
                            p.teleport(Bukkit.getWorld("testdungeon").getBlockAt(43, -42, 190).getLocation());
                            players.add(p);
                            alivePlayers.add(p);

                            if (players.size() == 4) {
                                System.out.println("[DUNGEON ALERT] Dungeon is currently full.");
                            }

                    } else {
                        p.sendMessage(ChatColor.RED + "The dungeon you are trying to join is currently filled.");
                    }
                }
            } else {
                p.sendMessage(ChatColor.RED +"You may not use this command!");
            }
        } else {
            System.out.println("NO");
        }
        return false;
    }
}