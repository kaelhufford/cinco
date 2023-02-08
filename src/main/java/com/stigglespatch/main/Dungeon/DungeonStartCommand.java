package com.stigglespatch.main.Dungeon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class DungeonStartCommand implements CommandExecutor, Listener {

    /*public static ArrayList players = new ArrayList<Player>();
    public static ArrayList alivePlayers = new ArrayList<Player>();
    public ArrayList<Player> getPlayersList(){
        return players;
    }
    public ArrayList<Player> getAlivePlayers(){
        return alivePlayers;
    }*/
    boolean move = false;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof  Player))
            return false;

        Player p = (Player) sender;

        if (args.length == 0) {
            p.sendMessage(ChatColor.RED +"No arguments provided!");
            return false;
        }

        String dungeonName = args[0];
        if (args.length >= 2) {
            if (args[1].equalsIgnoreCase("force")) {
                DungeonManager.forceStart(dungeonName);
                return true;
            }
            p.sendMessage(ChatColor.RED +"Not a valid argument!");
            return false;
        }

        ArrayList<Player> players = DungeonManager.getAlivePlayers(dungeonName);

        if (players.contains(p)) {
            p.sendMessage(ChatColor.RED + "You are already a part of this dungeon!");
            return false;
        }
        if (players.size() == 4) {
            p.sendMessage(ChatColor.RED + "The dungeon you are trying to join is currently filled.");
            return false;
        }

        if (players.size() == 0)
            Bukkit.broadcastMessage(ChatColor.YELLOW +"A Dungeon is being started by " + p.getName() +" you have 30 seconds to type the command: /dungeon join " + p.getName());

        DungeonManager.addPlayer(p, "testdungeon");
        p.sendMessage(ChatColor.GREEN + "You are being sent to the dungeon! Prepare yourself! (" + players.size() + "/4 Players)");

        p.getLocation().setYaw(90);
        p.getLocation().setPitch(0);

        //Add method to dungeon called giveInventory() and call it from DungeonManager.
        p.getInventory().clear();
        p.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
        p.getInventory().addItem(new ItemStack(Material.IRON_HELMET));
        p.getInventory().addItem(new ItemStack(Material.IRON_CHESTPLATE));
        p.getInventory().addItem(new ItemStack(Material.IRON_LEGGINGS));
        p.getInventory().addItem(new ItemStack(Material.IRON_BOOTS));
        p.getInventory().addItem(new ItemStack(Material.SHIELD));
        p.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));

        p.setGameMode(GameMode.ADVENTURE);


        p.sendMessage(ChatColor.GREEN + "You have been given items to aid you in the dungeon! Good luck.");

        if (players.size() == 4)
            System.out.println("[DUNGEON ALERT] Dungeon is currently full.");

        return true;
    }
}