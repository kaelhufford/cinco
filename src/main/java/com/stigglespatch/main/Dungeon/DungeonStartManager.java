package com.stigglespatch.main.Dungeon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class DungeonStartManager implements Listener {
    public int countdown;
    private Dungeon dungeon;
    private World world;

    private ArrayList<Player> players;
    private ArrayList<Player> alivePlayers;


    public DungeonStartManager (Dungeon dungeon, int countdown) {
        this.countdown = countdown;
        this.dungeon = dungeon;
        this.world = dungeon.getWorld();
    }

    //This will need to be a Bukkit runnable set for every second.
    //We could also add the Cuboid code to this.
    public void everySecond () {
        if (alivePlayers.isEmpty())
            return;

        if (countdown > 0) {
            --countdown;
        }
        if (countdown == 0) {
            dungeon.start ();
        }

        if (dungeon.getState().equals(DungeonState.STARTED)) {
            if (alivePlayers.isEmpty()) {
                dungeon.reset ();
            }

        }

    }


    /** Updates the player count and executes join functions
     *
     * @param e The player join server event
     */
    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!p.getWorld().getName().equals(dungeon.getWorld().getName()))
            return;

        dungeon.playerJoin(p);

        if (dungeon.getState() == DungeonState.RECRUITING) {
            if (dungeon.getPlayerCount() < dungeon.getMaxPlayerCount()) {
                //dungeon.playerJoin(p); //Moved this line to before the if statement
            } else {
                //Its probably best to stop the player from joining when they run the command. To-do later
                p.sendMessage(ChatColor.RED + "You can not join, the dungeon is full!");
            }

            //Set countdown to 10 once all players have joined
            if (dungeon.getPlayerCount() == dungeon.getPlayerCount() && countdown >= 0)
                countdown = 10;
        }
        else {
            //Kick the player from the dungeon if the game started
            p.sendMessage(ChatColor.RED + "Dungeon is already full! You may start a new dungeon raid once the current one completes.");
        }
    }

    @EventHandler
    public void onPlayerQuit (PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (!p.getWorld().getName().equals(dungeon.getWorld().getName()))
            return;

        //If a player leaves, allow for 30 more seconds for players to join.
        if (dungeon.getState() == DungeonState.RECRUITING) {
            if (countdown >= 0)
                countdown = 30;
        }

        dungeon.playerQuit(p);

    }

    @EventHandler
    public void onPlayerDeath (PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (!p.getWorld().getName().equals(dungeon.getWorld().getName()))
            return;

        alivePlayers.remove (p);
        p.setGameMode(GameMode.SPECTATOR);
    }

    public ArrayList<Player> getAlivePlayers () {
        return alivePlayers;
    }

    public ArrayList<Player> getPlayers () {
        return players;
    }

}
