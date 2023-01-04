package com.stigglespatch.main.Dungeon;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

public class Dungeon {

    public static int MAX_PLAYER_COUNT = 4;
    private int dungeonID;

    //World spawn-- This should not change.
    private Location worldSpawn;
    //Player spawn-- The current location where the player will respawn when they die. May not necessarily be world spawn,
    //But in many cases, it probably will be.
    private Location playerSpawn;


    //Stores player's main world location before joining the dungeon world.
    private Map<UUID, Location> oldPlayerLocation;
    //Stores player's main world inventory before joining the dungeon world.
    private Map<UUID, PlayerInventory> oldPlayerInventory;

    //RECRUITING or STARTED
    private DungeonState state;

    //Manages the games states, rooms, and timers
    private DungeonStartManager dungeonManager;

    private List<UUID> players;

    public Dungeon (int dungeonID, Location worldSpawn) {
        this.dungeonID = dungeonID;
        this.worldSpawn = worldSpawn;
        this.playerSpawn = worldSpawn; //Note: By default, playerSpawn will be equal to worldSpawn. It should be like
                                       //      this to start. After the dungeon begins, this has potential to change,
                                       //      but unlikely.
        this.state = DungeonState.RECRUITING;
        this.players = new ArrayList<>();
        this.oldPlayerLocation = new HashMap<>();
        this.oldPlayerInventory = new HashMap<>();

        //This will be uncommented when dungeonManager functionality is finished.
        //this.dungeonManager = new DungeonStartManager(this, 30);
    }

    /** Teleport the player to the world's player spawnpoint.
     *
     * @param player The player to be spawned.
     */
    public void spawnPlayer (Player player) {
        player.teleport (playerSpawn);
    }

    /** Retrieve the player spawn location.
     *
     * @return The location object of playerSpawn
     */
    public Location getPlayerSpawn() {
        return playerSpawn;
    }

    /** Retrieve the world spawn location.
     *
     * @return The location object of worldSpawn.
     */
    public Location getWorldSpawn() {
        return worldSpawn;
    }

    /** Retrieve the world the dungeon is located in.
     *
     * @return The world object
     */
    public World getWorld () {
        return worldSpawn.getWorld();
    }

    /** Returns how many players are in the dungeon.
     *
     * @return The size of the players ArrayList.
     */
    public int getPlayerCount () {
        return players.size();
    }

    /** Return the maximum number of players that may join the dungeon.
     *
     * @return MAX_PLAYER_COUNT
     */
    public int getMaxPlayerCount () {
        return MAX_PLAYER_COUNT;
    }

    /** Set player's next spawn location with given coordinates
     *
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     */
    public void setPlayerSpawn (float x, float y, float z) {
        playerSpawn = new Location (worldSpawn.getWorld(), x, y, z);
    }

    /** Set the world's spawn location based on the world and given coordinates.
     *
     * @param location A location object containing the dungeon world and x, y, z coordinates.
     */
    public void setWorldSpawn (Location location) {
        worldSpawn = location;
    }

    /** Set world spawn location with given coordinates within the same world.
     *
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     */
    public void setWorldSpawn (float x, float y, float z) {
        worldSpawn = new Location (worldSpawn.getWorld(), x, y, z);
    }

    /** Retrieve the list of players active in the dungeon.
     *
     * @return ArrayList of Players.
     */
    public List<UUID> getPlayers () {
        return players;
    }

    /** Add a new player to the Player ArrayList.
     *
     * @param p The player to be added.
     */
    public void addPlayer (Player p) {
        players.add (p.getUniqueId());
    }

    /** Remove a player from the Player ArrayList
     *
     * @param p The player to be removed.
     */
    public void removePlayer (Player p) {
        players.remove (p.getUniqueId());
    }

    /** Prepare the player for joining the dungeon. Will backup the player's information and
     *  set a new location and inventory.
     *
     * @param p The player joining the dungeon.
     */
    public void playerJoin (Player p) {
        oldPlayerLocation.put (p.getUniqueId(), p.getLocation());
        oldPlayerInventory.put (p.getUniqueId(), p.getInventory());

        this.addPlayer (p);
        this.spawnPlayer (p);

    }
    /*
        Make a checking system for if player quits during a dungeon.
     */

    /** Prepare the player for returning back to the main world. Restores their information
     *  to its original state, then teleports them back.
     *
     * @param p The player that is leaving the dungeon.
     */
    public void playerQuit (Player p) {
        this.removePlayer(p);
        Location oldLocation = oldPlayerLocation.remove (p.getUniqueId());
        PlayerInventory inv = oldPlayerInventory.remove (p.getUniqueId());
        //Clear current inventory
        p.getInventory().clear ();

        //Set old inventory
        p.getInventory().setContents(inv.getContents());
        p.getInventory().setArmorContents(inv.getArmorContents());
        p.getInventory().setExtraContents(inv.getExtraContents());

        //TP player back to their previous location
        p.teleport (oldLocation);
    }

    /** Retrieve the Dungeon Manager, which holds and operates important dungeon related events.
     *
     * @return Dungeon manager.
     */
    public DungeonStartManager getDungeonManager () {
        return dungeonManager;
    }

    /** Get the current state of the dungeon:
     *      (RECRUITING, STARTED).
     *
     * @return state
     */
    public DungeonState getState () {
        return state;
    }


    public void start () {
        //Put start code here
    }
}
