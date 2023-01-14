package com.stigglespatch.main.Dungeon;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class DungeonStartManager implements Listener {
    private int countdown;
    private int timer = 0;
    private boolean timerActive = false;

    private boolean started;

    private String actionBar = "⬜⬜⬜⬜⬜⬜⬜⬜⬜";
    private char c1 = '⬛';
    private char c2 = '⬜';

    private Dungeon dungeon;
    private World world;

    private ArrayList<Player> players;
    private ArrayList<Player> alivePlayers;

    private Dungeon.DungeonRoom currentRoom;

    public DungeonStartManager (Dungeon dungeon, int countdown) {
        this.countdown = countdown;
        this.dungeon = dungeon;
        this.world = dungeon.getWorld();

        players = new ArrayList<>();
        alivePlayers = new ArrayList<>();

        started = false;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(dungeon.getMain(), () -> everySecond(), 0L, 20L);

    }

    //This will need to be a Bukkit runnable set for every second.
    //We could also add the Cuboid code to this.
    public void everySecond () {
        if (alivePlayers.isEmpty())
            return;


        if (dungeon.getState() == DungeonState.RECRUITING) {
            if (countdown > 0)
                --countdown;

            if (countdown == 0) {
                dungeon.start();
                timerActive = true;
            }


        }
        else if (dungeon.getState().equals(DungeonState.STARTED)) {
            if (timerActive)
                ++timer;

            if (alivePlayers.isEmpty()) {
                dungeon.reset ();
                timerActive = false;


                this.getPlayers().clear();
                this.getAlivePlayers().clear();
            }
            //Update dungeon logic
            dungeon.update();

            //This code likely needs to be moved to Dungeon
            /*if (dungeon.getCurrentRoomIndex() != dungeon.getNextRoomIndex()) {
                if (dungeon.getNextRoom().isTriggered()) {
                    int next = dungeon.getNextRoomIndex() + 1;
                    dungeon.setCurrentRoom(dungeon.getNextRoomIndex());
                    dungeon.setNextRoom(next);


                    actionBar = "";

                    for (int i = 0; i < dungeon.getCurrentRoomIndex(); ++i) {
                        actionBar += c1;
                    }
                    for (int j = dungeon.getCurrentRoomIndex(); j < dungeon.getRooms().size(); ++j) {
                        actionBar += c2;
                    }

                }
            }
            for (Player player : dungeon.getDungeonManager().getAlivePlayers()) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBar));
            }*/

        }
        else if (dungeon.getState() == DungeonState.FAILED) {


        }
    }

    public boolean isStarted () { return started; }


    /** Updates the player count and executes join functions
     *
     * @param e The player join server event
     */
    @EventHandler
    public void onChangeWorld (PlayerChangedWorldEvent e) {
        if (true)
            return;

        Player p = e.getPlayer();

        //Player is joining the dungeon world
        if (p.getWorld ().getName().equals(dungeon.getWorld().getName())) {
            join (p);
        }
        //Player is leaving the dungeon world
        else if (e.getFrom().getName().equals(dungeon.getWorld().getName())) {
            leave (p);
        }

    }

    public void join (Player p) {
        //dungeon.playerJoin(p);


        p.setGameMode(GameMode.ADVENTURE);
        p.setInvisible(false);
        p.setAllowFlight(false);
        p.setFlying(false);

        if (dungeon.getState() == DungeonState.RECRUITING) {
            if (dungeon.getPlayerCount() < dungeon.getMaxPlayerCount()) {
                //dungeon.playerJoin(p); //Moved this line to before the if statement
            } else {
                //Its probably best to stop the player from joining when they run the command. To-do later
                p.sendMessage(ChatColor.RED + "You can not join, the dungeon is full!");
            }

            //Set countdown to 10 once all players have joined
            if (dungeon.getPlayerCount() == dungeon.getMaxPlayerCount() && countdown >= 0)
                countdown = 10;
        }
        else {
            //Kick the player from the dungeon if the game started
            p.sendMessage(ChatColor.RED + "Dungeon is already full! You may start a new dungeon raid once the current one completes.");
        }
    }
    public void leave (Player p) {

        p.setGameMode(GameMode.SURVIVAL);
        p.setInvisible(false);
        p.setAllowFlight(false);
        p.setFlying(false);

        if (!p.getWorld().getName().equals(dungeon.getWorld().getName()))
            return;

        //If a player leaves, allow for 30 more seconds for players to join.
        if (dungeon.getState() == DungeonState.RECRUITING) {
            if (countdown >= 0)
                countdown = 30;
        }

        this.getPlayers().remove(p);
        this.getAlivePlayers().remove(p);

        //dungeon.playerQuit(p);
    }


    @EventHandler
    public void onPlayerDeath (PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (!p.getWorld().getName().equals(dungeon.getWorld().getName()))
            return;

        alivePlayers.remove (p);

        if (this.getPlayers().contains(p)) {
            p.setAllowFlight(true);
            p.setFlying(true);
            p.setInvisible(true);
        }
        if (this.getAlivePlayers().contains(p)){
            this.getAlivePlayers().remove(p);
        }

        if (this.getAlivePlayers().isEmpty()) {
            dungeon.failed();
        }


    }

    @EventHandler
    public void onPlayerQuit (PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (!p.getWorld().getName().equals(dungeon.getWorld().getName()))
            return;

        if (isStarted())
            dungeon.playerQuit(p);
    }

    public ArrayList<Player> getAlivePlayers () {
        return alivePlayers;
    }

    public ArrayList<Player> getPlayers () {
        return players;
    }

    public int getTime () { return timer; }

    public int getCountdown () { return countdown; }

}
