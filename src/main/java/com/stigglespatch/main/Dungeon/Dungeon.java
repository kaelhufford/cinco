package com.stigglespatch.main.Dungeon;

import com.stigglespatch.main.Dungeon.Cuboids.Cuboid;
import com.stigglespatch.main.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

public class Dungeon {
    protected enum RoomType {
        LOBBY,
        FILLER,
        FIND,
        PARKOUR,
        COLLECTION,
        STAGE_CONNECTION,
        TARGET,
        LEVEL_CONNECTION,
        MOB,
        WAVE,
        BOSS,
        FINAL;

    }
    /**
     * This is going to be a long section of class definitions.
     * There will be a room definition for each type.
     */
    protected abstract class DungeonRoom {
        private Cuboid boundary;
        private Cuboid entrance;
        private Cuboid exit;
        private Material fillType = Material.DEEPSLATE;
        private Location spawnLocation;

        private boolean finished;
        private boolean roomActive;

        private RoomType type;

        public DungeonRoom(Cuboid boundary) {
            this(boundary, boundary.getCenter());
        }

        public DungeonRoom(Cuboid boundary, Location spawn) {
            this.boundary = boundary;
            this.spawnLocation = spawn;
        }

        public Cuboid getBoundary() {
            return boundary;
        }

        public RoomType getType() {
            return type;
        }
        public Cuboid getEntrance () { return entrance; }
        public Cuboid getExit () { return exit; }

        public void openEntrance () {
            if (entrance == null)
                return;
            for (Block block : entrance)
                block.setType(Material.AIR);
        }

        public void closeEntrance () {
            if (entrance == null)
                return;
            for (Block block : entrance)
                block.setType (fillType);
        }

        public void openExit () {
            if (exit == null)
                return;
            for (Block block : exit)
                block.setType(Material.AIR);
        }

        public void closeExit () {
            if (exit == null)
                return;
            for (Block block : exit)
                block.setType (fillType);
        }


        public void setType(RoomType type) {
            this.type = type;
        }

        public abstract void update();

        public abstract void onPlayerEnter(Player player);

        public void setSpawnLocation(Location l) {
            spawnLocation = l;
        }

        public Location getSpawnLocation() {
            return spawnLocation;
        }

        public boolean isFinished() {
            return finished;
        }

        public boolean isRoomActive() {
            return roomActive;
        }

        public void setFinished(boolean val) {
            finished = val;
        }

        public void setRoomActive(boolean val) {
            roomActive = val;
        }

        public boolean contains(Player p) {
            return getBoundary().contains(p.getLocation());
        }

        public void teleportRoomSpawn(Player p) {
            p.teleport(spawnLocation);
        }

        public void resetRoom() {
        }

    }

    protected abstract class SpawnerDungeonRoom extends DungeonRoom {
        private Map<Block, String> spawnerBlocks;

        public SpawnerDungeonRoom(Cuboid boundary, Location spawn) {
            super(boundary, spawn);
            spawnerBlocks = new HashMap<>();
        }

        public void addSpawnerBlocks() {
            for (Block block : getBoundary()) {
                if (block.getType().equals(Material.LIGHT_GRAY_CANDLE)) {
                    spawnerBlocks.put(block, "zombie");
                    continue;
                }
                if (block.getType().equals(Material.GRAY_CANDLE)) {
                    spawnerBlocks.put(block, "skeleton");
                    continue;
                }
                if (block.getType().equals(Material.CYAN_CANDLE)) {
                    spawnerBlocks.put(block, "creeper");
                    continue;
                }
            }
        }

    }

    protected class LobbyRoom extends DungeonRoom {

        public LobbyRoom (Cuboid boundary, Location spawn) {
            super (boundary, spawn);
            setType(RoomType.LOBBY);
        }
        @Override
        public void update() {

        }

        @Override
        public void onPlayerEnter(Player player) {

        }
    }

    protected class FillerRoom extends DungeonRoom {


        public FillerRoom(Cuboid boundary, Location spawn) {
            super(boundary, spawn);
            setType(RoomType.FILLER);
        }

        @Override
        public void onPlayerEnter(Player player) {


        }

        @Override
        public void update() {
        }
    }

    protected class FindRoom extends DungeonRoom {

        //public Key key;
        public FindRoom(Cuboid boundary, Location spawn) {
            super(boundary, spawn);
            setType(RoomType.FIND);
        }

        @Override
        public void onPlayerEnter(Player player) {


        }

        @Override
        public void update() {

        }
    }
    protected class ParkourRoom extends DungeonRoom {

        //public Key key;
        public ParkourRoom(Cuboid boundary, Location spawn) {
            super(boundary, spawn);
            setType(RoomType.PARKOUR);
        }

        @Override
        public void onPlayerEnter(Player player) {


        }

        @Override
        public void update() {

        }
    }
    protected class CollectionRoom extends DungeonRoom {

        public CollectionRoom(Cuboid boundary, Location spawn) {
            super(boundary, spawn);
            setType(RoomType.COLLECTION);
        }

        @Override
        public void onPlayerEnter(Player player) {


        }

        @Override
        public void update() {

        }
    }



    protected class TargetRoom extends DungeonRoom {

        public ArrayList<Block> targetBlocks;

        public TargetRoom(Cuboid boundary, Location spawn) {
            super(boundary, spawn);
            setType(RoomType.TARGET);
            targetBlocks = new ArrayList<>();
            getTargetBlocks();
        }

        @Override
        public void onPlayerEnter(Player player) {


        }

        public void getTargetBlocks() {
            for (Block block : getBoundary()) {
                if (block.getType().equals(Material.TARGET)) {
                    targetBlocks.add(block);
                }
            }
        }

        public boolean checkTargets() {
            for (Block block : targetBlocks) {
                if (!block.isBlockPowered()) {
                    return false;
                }
            }
            return true;
        }

        public void onHitAllTargets() {
            targetBlocks.clear();
            openExit();
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "An entrance has opened!");
        }

        @Override
        public void update() {
            if (!targetBlocks.isEmpty() && checkTargets()) {
                onHitAllTargets();
            }
        }
    }

    protected class MobRoom extends SpawnerDungeonRoom {


        public MobRoom(Cuboid boundary, Location spawn) {
            super(boundary, spawn);
            setType(RoomType.MOB);
        }

        @Override
        public void onPlayerEnter(Player player) {


        }

        @Override
        public void update() {
        }
    }

    protected class WaveRoom extends SpawnerDungeonRoom {

        int waveCount = 5;
        int timeBetweenWaves = 8;
        public WaveRoom(Cuboid boundary, Location spawn) {
            super(boundary, spawn);
            setType(RoomType.WAVE);
        }

        @Override
        public void onPlayerEnter(Player player) {


        }

        @Override
        public void update() {

        }

        public void spawnNextWave () {

        }
    }
    protected class BossRoom extends SpawnerDungeonRoom {

        public BossRoom(Cuboid boundary, Location spawn) {
            super(boundary, spawn);
            setType(RoomType.BOSS);
        }

        @Override
        public void onPlayerEnter(Player player) {


        }

        @Override
        public void update() {

        }
    }
    protected class FinalRoom extends DungeonRoom {

        public FinalRoom(Cuboid boundary, Location spawn) {
            super(boundary, spawn);
            setType(RoomType.FINAL);
        }

        @Override
        public void onPlayerEnter(Player player) {


        }

        @Override
        public void update() {

        }
    }

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
    private List<Cuboid> dungeonCuboids;
    public static ArrayList roomsFinished = new ArrayList<DungeonCuboid>();
    private DungeonRoom currentRoom;
    protected List<DungeonRoom> rooms;
    private boolean active;

    public Dungeon(Main main, int dungeonID, Location worldSpawn) {
        this.dungeonID = dungeonID;
        this.worldSpawn = worldSpawn;
        this.playerSpawn = worldSpawn; //Note: By default, playerSpawn will be equal to worldSpawn. It should be like
        //      this to start. After the dungeon begins, this has potential to change,
        //      but unlikely.
        this.state = DungeonState.RECRUITING;
        this.players = new ArrayList<>();
        this.dungeonCuboids = new ArrayList<>();
        this.oldPlayerLocation = new HashMap<>();
        this.oldPlayerInventory = new HashMap<>();
        rooms = new LinkedList<>();
        active = false;

        //This will be uncommented when dungeonManager functionality is finished.
        this.dungeonManager = new DungeonStartManager(this, 30);
        Bukkit.getPluginManager().registerEvents(this.dungeonManager, main);
    }

    /**
     * Teleport the player to the world's player spawnpoint.
     *
     * @param player The player to be spawned.
     */
    public void spawnPlayer(Player player) {
        player.teleport(playerSpawn);
    }

    /**
     * Retrieve the player spawn location.
     *
     * @return The location object of playerSpawn
     */
    public Location getPlayerSpawn() {
        return playerSpawn;
    }

    /**
     * Retrieve the world spawn location.
     *
     * @return The location object of worldSpawn.
     */
    public Location getWorldSpawn() {
        return worldSpawn;
    }

    /**
     * Retrieve the world the dungeon is located in.
     *
     * @return The world object
     */
    public World getWorld() {
        return worldSpawn.getWorld();
    }

    /**
     * Returns how many players are in the dungeon.
     *
     * @return The size of the players ArrayList.
     */
    public int getPlayerCount() {
        return players.size();
    }

    /**
     * Return the maximum number of players that may join the dungeon.
     *
     * @return MAX_PLAYER_COUNT
     */
    public int getMaxPlayerCount() {
        return MAX_PLAYER_COUNT;
    }

    /**
     * Set player's next spawn location with given coordinates
     *
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     */
    public void setPlayerSpawn(float x, float y, float z) {
        playerSpawn = new Location(worldSpawn.getWorld(), x, y, z);
    }

    /**
     * Set the world's spawn location based on the world and given coordinates.
     *
     * @param location A location object containing the dungeon world and x, y, z coordinates.
     */
    public void setWorldSpawn(Location location) {
        worldSpawn = location;
    }

    /**
     * Set world spawn location with given coordinates within the same world.
     *
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     */
    public void setWorldSpawn(float x, float y, float z) {
        worldSpawn = new Location(worldSpawn.getWorld(), x, y, z);
    }

    /**
     * Retrieve the list of players active in the dungeon.
     *
     * @return ArrayList of Players.
     */
    public List<UUID> getPlayers() {
        return players;
    }

    /**
     * Add a new player to the Player ArrayList.
     *
     * @param p The player to be added.
     */
    public void addPlayer(Player p) {
        players.add(p.getUniqueId());
    }

    /**
     * Remove a player from the Player ArrayList
     *
     * @param p The player to be removed.
     */
    public void removePlayer(Player p) {
        players.remove(p.getUniqueId());
    }

    /**
     * Prepare the player for joining the dungeon. Will back up the player's information and
     * set a new location and inventory.
     *
     * @param p The player joining the dungeon.
     */
    public void playerJoin(Player p) {
        oldPlayerLocation.put(p.getUniqueId(), p.getLocation());
        oldPlayerInventory.put(p.getUniqueId(), p.getInventory());

        this.addPlayer(p);
        this.spawnPlayer(p);

    }

    public boolean isActive () {
        return active;
    }
    public void setActive (boolean val) {
        active = val;
    }

    public DungeonRoom getCurrentRoom() {
        return currentRoom;
    }
    public void setCurrentRoom(DungeonRoom room) {
        currentRoom = room;
    }

    /*
        Make a checking system for if player quits during a dungeon.
     */

    /**
     * Prepare the player for returning to the main world. Restores their information
     * to its original state, then teleports them back.
     *
     * @param p The player that is leaving the dungeon.
     */
    public void playerQuit(Player p) {
        this.removePlayer(p);
        Location oldLocation = oldPlayerLocation.remove(p.getUniqueId());
        PlayerInventory inv = oldPlayerInventory.remove(p.getUniqueId());
        //Clear current inventory
        p.getInventory().clear();

        //Set old inventory
        p.getInventory().setContents(inv.getContents());
        p.getInventory().setArmorContents(inv.getArmorContents());
        p.getInventory().setExtraContents(inv.getExtraContents());

        //TP player back to their previous location
        p.teleport(oldLocation);
    }

    /**
     * Retrieve the Dungeon Manager, which holds and operates important dungeon related events.
     *
     * @return Dungeon manager.
     */
    public DungeonStartManager getDungeonManager() {
        return dungeonManager;
    }

    /**
     * Get the current state of the dungeon:
     * (RECRUITING, STARTED).
     *
     * @return state
     */
    public DungeonState getState() {
        return state;
    }

    public void checkPlayerLocations(List<Player> playerList) {
        for (Player p : playerList) {
            if (roomsFinished.contains(p.getLocation())) {
                p.teleport(currentRoom.getSpawnLocation());
            }
        }
    }






    public void start () {
        setActive (true);
    }
    public void reset () {
        setActive (false);
    }
}

