package com.stigglespatch.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class DungeonStartCommand implements CommandExecutor, Listener {

    ArrayList players = new ArrayList<Player>();
    ArrayList alivePlayers = new ArrayList<Player>();

    public ArrayList<Player> getPlayers(){
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
                if((players.size()) <= 3) {//3
                    p.sendMessage(ChatColor.GREEN +"You are being sent to the dungeon! Prepare yourself! (" + Math.addExact(1, players.size()) + "/4 Players)");
                    p.teleport(Bukkit.getWorld("testdungeon").getBlockAt(43,-42,190).getLocation());
                    players.add(p);
                    alivePlayers.add(p);
                    if(players.size() == 4){
                        System.out.println("[DUNGEON ALERT] Dungeon is currently full.");
                    }

                } else {
                    p.sendMessage(ChatColor.RED + "The dungeon you are trying to join is currently filled.");
                }

            } else {
                p.sendMessage(ChatColor.RED +"You may not use this command!");
            }

        } else {
            System.out.println("NO");
        }
        return false;
    }


    @EventHandler
    public void onDisconnect(PlayerQuitEvent event){
        if (players.contains(event.getPlayer())) {
            players.remove(event.getPlayer());
        } else {
            return;
        }
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        if (alivePlayers.contains(event.getEntity())) {
            alivePlayers.remove(event.getEntity());
        } else {
            return;
        }
    }
}