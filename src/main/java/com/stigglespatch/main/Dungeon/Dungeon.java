package com.stigglespatch.main.Dungeon;

import com.stigglespatch.main.Dungeon.Cuboids.Cuboid;
import com.stigglespatch.main.Main;
import jdk.internal.icu.impl.BMPSet;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

public class Dungeon {


    public static final int MAX_PLAYER_COUNT = 4;
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
    private List<UUID> playerID;
    private List<Cuboid> dungeonCuboids;
    public static ArrayList roomsFinished = new ArrayList<DungeonRoom>();
    private int currentRoom;
    private int nextRoom;
    private DungeonRoom current;
    protected List<DungeonRoom> rooms;
    private boolean active;
    private Main main;

    public Dungeon(Main main, int dungeonID, Location worldSpawn) {

        this.main = main;
        this.dungeonID = dungeonID;
        this.worldSpawn = worldSpawn;
        this.playerSpawn = worldSpawn; //Note: By default, playerSpawn will be equal to worldSpawn. It should be like
        //      this to start. After the dungeon begins, this has potential to change,
        //      but unlikely.
        this.state = DungeonState.RECRUITING;
        this.playerID = new ArrayList<>();
        this.dungeonCuboids = new ArrayList<>();
        this.oldPlayerLocation = new HashMap<>();
        this.oldPlayerInventory = new HashMap<>();
        rooms = new ArrayList<>();
        active = false;

        //Initialize Rooms...

        setCurrentRoom(0);
        setNextRoom(1);
        //This will be uncommented when dungeonManager functionality is finished.
        this.dungeonManager = new DungeonStartManager(this, 30);
        Bukkit.getPluginManager().registerEvents(this.dungeonManager, main);
    }


    public Main getMain () { return main; }
    /**
     * Teleport the player to the world's player spawn point.
     *
     * @param player The player to be spawned.
     */
    public void spawnPlayer(Player player) {
        player.teleport(playerSpawn);
    }

    public void update () {
        int room = currentRoom;
        for (int index = currentRoom; index < rooms.size(); ++index) {
            if (rooms.get(index).checkPlayer(this.getAlivePlayers().get(0), rooms.get(index).getBoundary())) {
                if (room > currentRoom) {
                    currentRoom = room;
                    current = rooms.get(currentRoom);
                    Bukkit.getConsoleSender().sendMessage("Player has advanced to the next room!");
                }
                rooms.get(index).update ();
                Bukkit.getConsoleSender().sendMessage("Current: " + current.type.toString());
                return;
            }
            ++room;
        }
    }

    public void goToNextRoom () {
        ++currentRoom;
        nextRoom = currentRoom + 1;

        if (this.getCurrentRoomIndex() >= rooms.size () - 1) {
            currentRoom = rooms.size() - 1;
            nextRoom = rooms.size() - 1;
        }
        current = getNextRoom();

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
        return playerID.size();
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
     * Retrieve the ArrayList index of the room the players are currently in.
     *
     * @return integer index
     */
    public int getCurrentRoomIndex() {
        return currentRoom;
    }

    public Material getBlockBelowPlayer (Player p) {
        return getWorld().getBlockAt( (int) p.getLocation().getX(), (int) p.getLocation().getZ() - 1, (int) p.getLocation().getZ()).getType();
    }

    public RoomType getPlayerRoom () {
        return getRoomType(getBlockBelowPlayer(this.getAlivePlayers().get(0)));
    }
    public boolean isSameRoom () {
        if (playerID.isEmpty())
            return false;

        return getPlayerRoom().equals (current.type);
    }

    public void createRoomFromType () {

    }
    public void checkCurrentRoom () {
        ArrayList<Player> alive = this.getAlivePlayers();
        if (alive.isEmpty())
            return;

        getBlockBelowPlayer(alive.get(0));
    }
    public ArrayList<Player> getPlayers () { return dungeonManager.getPlayers(); }
    public ArrayList<Player> getAlivePlayers () { return dungeonManager.getAlivePlayers(); }
    //public ArrayList<Player> getPlayers () { return dungeonManager.getPlayers (); }

    public RoomType getRoomType(Material m) {
        if (m == Material.LIME_CONCRETE)
            return RoomType.LOBBY;
        if (m == Material.BLUE_CONCRETE)
            return RoomType.FILLER;
        if (m == Material.LIGHT_BLUE_CONCRETE)
            return RoomType.FIND;
        if (m == Material.CYAN_CONCRETE)
            return RoomType.PARKOUR;
        if (m == Material.YELLOW_CONCRETE)
            return RoomType.COLLECTION;
        if (m == Material.GRAY_CONCRETE)
            return RoomType.STAGE_CONNECTION;
        if (m == Material.PINK_CONCRETE)
            return RoomType.TARGET;
        if (m == Material.RED_CONCRETE)
            return RoomType.MOB;
        if (m == Material.MAGENTA_CONCRETE)
            return RoomType.WAVE;
        if (m == Material.BLACK_CONCRETE)
            return RoomType.BOSS;
        if (m == Material.ORANGE_CONCRETE)
            return RoomType.FINAL;
        return RoomType.NULL;
    }
    /**
     * Retrieve the ArrayList index of the room the players will go to next.
     *
     * @return integer index
     */
    public int getNextRoomIndex() {
        return nextRoom;
    }

    /**
     * Retrieve the current room the players are in.
     *
     * @return DungeonRoom that the players are in
     */
    public DungeonRoom getCurrentRoom() {
        return rooms.get(currentRoom);
    }

    /**
     * Retrieve the next room the players will go to.
     *
     * @return DungeonRoom that players will advance to next.
     */
    public DungeonRoom getNextRoom() {
        return rooms.get(nextRoom);
    }

    /**
     * Retrieve all the rooms in a dungeon.
     *
     * @return A list of all the DungeonRooms
     */
    public List<DungeonRoom> getRooms() {
        return rooms;
    }

    /**
     * Set the room index of the room the players are currently in.
     *
     * @param index integer value
     */
    public void setCurrentRoom(int index) {
        currentRoom = index;
    }

    /**
     * Set the room index of the room the players are going to next.
     *
     * @param index integer value
     */
    public void setNextRoom(int index) {
        nextRoom = index;

        if (nextRoom > rooms.size())
            nextRoom = currentRoom;

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
    public List<UUID> getPlayerIDs () {
        return playerID;
    }

    /**
     * Add a new player to the Player ArrayList.
     *
     * @param p The player to be added.
     */
    public void addPlayerID(Player p) {
        playerID.add(p.getUniqueId());
    }

    /**
     * Remove a player from the Player ArrayList
     *
     * @param p The player to be removed.
     */
    public void removePlayerID(Player p) {
        playerID.remove(p.getUniqueId());
    }

    /**
     * Prepare the player for joining the dungeon. Will back up the player's information and
     * set a new location and inventory.
     *
     * @param p The player joining the dungeon.
     */
    public void playerJoin(Player p) {
        Bukkit.getServer().getConsoleSender().sendMessage("Dungeon: " + p.getName() + " has joined!");
        oldPlayerLocation.put(p.getUniqueId(), p.getLocation());
        oldPlayerInventory.put(p.getUniqueId(), p.getInventory());

        this.addPlayerID(p);
        this.spawnPlayer(p);

        dungeonManager.join(p);
    }

    /**
     * Check if the dungeon is marked as active or not
     *
     * @return The boolean active
     */
    public boolean isActive() {
        return active;
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
        dungeonManager.leave (p);

        this.removePlayerID(p);
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
    public void setState (DungeonState state) {
        this.state = state;
    }

    public boolean isFull () { return (playerID.size() == MAX_PLAYER_COUNT); }
    public void checkPlayerLocations(List<Player> playerList) {
        for (Player p : playerList) {
            if (roomsFinished.contains(p.getLocation())) {
                p.teleport(rooms.get(currentRoom).getSpawnLocation());
            }
        }
    }

    public void killMobs() {
        List<Entity> entities = getWorldEntities();
        if (entities == null || entities.isEmpty())
            return;

        for (Entity entity : entities) {
            //Add other entities that should not be killed
            if (!(entity instanceof Player) && !(entity instanceof ItemFrame) && !(entity instanceof ArmorStand)) {
                entity.remove();
            }
        }
    }

    public List<Entity> getWorldEntities() {
        if (worldSpawn.getWorld() == null)
            return null;
        return worldSpawn.getWorld().getEntities();
    }

    public void start() {
        active = true;
        currentRoom = 0;
        nextRoom = 1;
        state = DungeonState.STARTED;
        current = rooms.get (0);
        dungeonManager.timerActive = true;
        //rooms.get(currentRoom).update();
        //Remove barriers at entrance
    }

    public void reset() {
        for (Player p : dungeonManager.getPlayers()) {
            playerQuit(p);
        }
        active = false;
        killMobs();

        if (rooms != null && !rooms.isEmpty()) {
            for (DungeonRoom room : rooms) {
                room.resetRoom();
            }
        }
        Bukkit.broadcastMessage(ChatColor.YELLOW +"The dungeon has been reset! Do /start-dungeon to begin!");

    }

    public void failed() {
        state = DungeonState.FAILED;
        for (Player p : getPlayers())
            playerQuit (p);
    }

    /**
     * Describes the variety of different types of rooms there can be.
     */
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
        FINAL,
        NULL
    }
    /* This is going to be a long section of class definitions.
     * There will be a room definition for each type.
     */
    protected abstract class DungeonRoom {
        private final Material TRIGGER_BLOCK = Material.BROWN_GLAZED_TERRACOTTA;
        private final Material ENTRANCE_BLOCK = Material.WHITE_GLAZED_TERRACOTTA;
        private final Material EXIT_BLOCK = Material.BLACK_GLAZED_TERRACOTTA;
        protected Material fillType = Material.BLUE_WOOL;
        protected byte doorOffset = 3;
        private Cuboid boundary;
        private Cuboid entrance;
        private Cuboid exit;

        private Cuboid trigger;

        private Location spawnLocation;

        private boolean finished = false;
        private boolean roomActive;
        protected boolean enteredRoom = false;

        protected boolean triggered = false;
        private RoomType type;

        protected DungeonRoom next;

        //Constructors
        public DungeonRoom(Cuboid boundary) {
            this(boundary, boundary.getCenter());
        }
        public DungeonRoom(Cuboid boundary, Location spawn) {
            this.boundary = boundary;
            this.spawnLocation = spawn;


            trigger = findTrigger();
            entrance = findEntrance();
            exit = findExit();



            Location l = new Location (getWorld(), 0.5, 0.5, 0.5);
            if (trigger != null)
                l = trigger.getCenter();

            this.spawnLocation = new Location(getWorld(), l.getBlockX() + 0.5, l.getBlockY() - 0.5, l.getBlockZ() + 0.5);
        }

        public DungeonRoom(Cuboid boundary, Cuboid entrance, Location spawn) {
            //Sometimes, the trigger may be the entire room. Also, there may be only 1 entrance/exit (For example, lobby room).
            this.boundary = boundary;
            this.trigger = boundary;
            this.spawnLocation = spawn;
            this.entrance = entrance;
            this.exit = entrance;
        }
        public DungeonRoom(Cuboid boundary, Cuboid trigger, Cuboid entrance, Cuboid exit, Location spawn) {
            this.boundary = boundary;
            this.spawnLocation = spawn;
            this.trigger = trigger;
            this.entrance = entrance;
            this.exit = exit;
        }


        public Cuboid getBoundary() {
            return boundary;
        }

        /** Locates the defined trigger (COMMAND_BLOCK) in the cuboid boundary.
         *
         * @return The 3x3 cuboid trigger
         */
        public Cuboid findTrigger () {
            for (Block b : boundary) {
                if (b.getType().equals(TRIGGER_BLOCK)) {
                    return new Cuboid (getWorld(), b.getLocation().getBlockX() - 1, b.getLocation().getBlockY() + 2, b.getLocation().getBlockZ() - 1,
                            b.getLocation().getBlockX() + 1, b.getLocation().getBlockY() + 5, b.getLocation().getBlockZ() + 1);
                }
            }
            return null;
        }

        /** Locates the defined entrance (WHITE_GLAZED_TERRACOTTA) in the cuboid boundary.
         *
         * @return The 3x1 cuboid
         */
        public Cuboid findEntrance () {
            ArrayList<Location> blocks = new ArrayList<>();
            for (Block b : boundary) {
                if (b.getType().equals(ENTRANCE_BLOCK)) {
                    blocks.add (b.getLocation());
                    //return new Cuboid (getWorld(), b.getLocation().getBlockX(), b.getLocation().getBlockY() + 2, b.getLocation().getBlockZ(),
                    //        b.getLocation().getBlockX(), b.getLocation().getBlockY() + 5, b.getLocation().getBlockZ());
                }
            }

            if (blocks.size() == 1) {
                Location l = blocks.get(0);
                return new Cuboid (getWorld(), l.getBlockX(), l.getBlockY() + 2, l.getBlockZ(),
                        l.getBlockX(), l.getBlockY() + 2 + doorOffset, l.getBlockZ());
            }
            else if (blocks.size() == 2) {
                Location l1 = blocks.get(0);
                Location l2 = blocks.get(1);

                int dX = l1.getBlockX() - l2.getBlockX();
                int dZ = l1.getBlockZ() - l2.getBlockZ();

                return new Cuboid (getWorld(), l1.getBlockX(), l1.getBlockY() + 2, l1.getBlockZ(),
                        l1.getBlockX() - dX, l1.getBlockY() + 2 + doorOffset, l1.getBlockZ() - dZ);
            }

            return null;
        }
        /** Locates the defined exit (BLACK_GLAZED_TERRACOTTA) in the cuboid boundary.
         *
         * @return The 3x1 cuboid
         */
        public Cuboid findExit () {
            ArrayList<Location> blocks = new ArrayList<>();
            for (Block b : boundary) {
                if (b.getType().equals(EXIT_BLOCK)) {
                    blocks.add(b.getLocation());
                    //return new Cuboid (getWorld(), b.getLocation().getBlockX(), b.getLocation().getBlockY() + 2, b.getLocation().getBlockZ(),
                    //        b.getLocation().getBlockX(), b.getLocation().getBlockY() + 5, b.getLocation().getBlockZ());
                }
            }
            if (blocks.size() == 1) {
                Location l = blocks.get(0);
                return new Cuboid (getWorld(), l.getBlockX(), l.getBlockY() + 2, l.getBlockZ(),
                        l.getBlockX(), l.getBlockY() + 2 + doorOffset, l.getBlockZ());
            }
            else if (blocks.size() == 2) {
                Location l1 = blocks.get(0);
                Location l2 = blocks.get(1);

                int dX = l1.getBlockX() - l2.getBlockX();
                int dZ = l1.getBlockZ() - l2.getBlockZ();

                return new Cuboid (getWorld(), l1.getBlockX(), l1.getBlockY() + 2, l1.getBlockZ(),
                        l1.getBlockX() - dX, l1.getBlockY() + 2 + doorOffset, l1.getBlockZ() - dZ);
            }
            return null;
        }

        public Cuboid getEntrance() {
            return entrance;
        }

        public Cuboid getExit() {
            return exit;
        }

        public Material getFillType() {
            return fillType;
        }

        public Cuboid getTrigger () { return trigger; }

        public RoomType getType() {
            return type;
        }

        public void setNextRoom (DungeonRoom room) {
            next = room;
        }
        public void openEntrance() {
            if (entrance == null)
                return;
            for (Block block : entrance)
                block.setType(Material.AIR);
        }

        public void closeEntrance() {
            if (entrance == null)
                return;
            for (Block block : entrance)
                block.setType(fillType);
        }

        public void openExit() {
            if (exit == null)
                return;
            for (Block block : exit)
                block.setType(Material.AIR);
        }

        public void closeExit() {
            if (exit == null)
                return;
            for (Block block : exit)
                block.setType(fillType);
        }

        public void setSpawnLocation(Location l) {
            spawnLocation = l;
        }
        public void setType(RoomType type) {
            this.type = type;
        }

        public void update() {
            if (finished)
                return;
            if (!enteredRoom) {
                if (checkAllPlayers(boundary)) {
                    onPlayerEnter(dungeonManager.getAlivePlayers().get(0));
                    enteredRoom = true;
                }
            }
            if (trigger == null)
                return;
            if (!triggered)
                if(checkPlayer(dungeonManager.getAlivePlayers().get(0), trigger))
                    onTrigger();

            //if(checkAllPlayers(trigger));
            /*if (checkAllPlayers(next.trigger)) { //Next time you work on this, you are gonna work here. The way oyu implemented this
                                                //Does not require using next. So, figure out how to check only current trigger. (Look in dungeon.update)
                active = false;
                finished = true;
                next.onTrigger();
            }*/
        }
        public abstract void onPlayerEnter(Player player);
        public void onTrigger () {
            if (checkAllPlayers(trigger)) {
                roomActive = true;
                closeEntrance();
                closeExit();
                triggered = true;
            }
        }

        public boolean checkPlayer (Player p, Cuboid container) {
            return container.contains(p.getLocation());
        }
        public boolean checkAllPlayers (Cuboid container) {
            for (Player player : dungeonManager.getAlivePlayers()) {
                if (!container.contains(player.getLocation()))
                    return false;
            }
            return true;
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
        public boolean hasEnteredRoom () {
            return enteredRoom;
        }
        public boolean isTriggered () {
            return triggered;
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
        public abstract void resetRoom();

    }
    protected abstract class SpawnerDungeonRoom extends DungeonRoom {
        private Map<Block, String> spawnerBlocks;

        protected boolean spawned;
        protected int waveCount = 5;
        protected int timeBetweenWaves = 8;
        protected int countdown = 0;
        protected int currentWave = 0;


        public SpawnerDungeonRoom(Cuboid boundary) {
            super(boundary);
            spawnerBlocks = new HashMap<>();
            addSpawnerBlocks();
        }

        public SpawnerDungeonRoom(Cuboid boundary, Cuboid trigger, Cuboid entrance, Cuboid exit, Location spawn) {
            super(boundary, trigger, entrance, exit, spawn);
            spawnerBlocks = new HashMap<>();
            addSpawnerBlocks();
        }

        public boolean hasSpawned() {
            return spawned;
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

        public void spawnMobs() {

            if (spawnerBlocks == null || spawnerBlocks.isEmpty())
                return;
            for (Block block : spawnerBlocks.keySet()) {
                DungeonMobs.spawnDungeonMobs(block.getLocation().add(0, 1, 0), spawnerBlocks.get(block));

            }
            Bukkit.getConsoleSender().sendMessage("Spawned mobs in " + this.getType().toString());
            spawned = true;
        }

    }
    protected abstract class MultiRoom extends DungeonRoom {

        public MultiRoom (Cuboid boundary, Cuboid trigger, Cuboid entrance, Cuboid exit, Location spawn) {
            super (boundary, trigger, entrance, exit, spawn);
        }

    }
    protected class LobbyRoom extends DungeonRoom {

        private boolean start;

        public LobbyRoom(Cuboid boundary) {
            super(boundary);
            setType(RoomType.LOBBY);
            fillType = Material.BARRIER;
            doorOffset = 6;
            closeExit();
            start = false;
        }


        @Override
        public void onPlayerEnter(Player player) {

        }
        @Override
        public void onTrigger () {
            openExit();
            triggered = true;
        }

        @Override
        public void resetRoom() {
            closeExit();
            start = false;
            triggered = false;
        }


        @Override
        public void update() {
            if (!dungeonManager.isStarted())
                return;

            /*if (!triggered)
                onTrigger();*/

            super.update();
        }
    }
    protected class FillerRoom extends DungeonRoom {


        public FillerRoom(Cuboid boundary) {
            super(boundary);
            setType(RoomType.FILLER);
        }

        @Override
        public void onPlayerEnter(Player player) {


        }


        @Override
        public void resetRoom() {

        }
    }
    protected class FindRoom extends DungeonRoom {

        //public Key key;
        public FindRoom(Cuboid boundary) {
            super(boundary);
            setType(RoomType.FIND);
        }

        @Override
        public void onPlayerEnter(Player player) {


        }

        @Override
        public void resetRoom() {

        }

        @Override
        public void update() {

        }
    }
    protected class ParkourRoom extends DungeonRoom {

        //public Key key;

        public ParkourRoom(Cuboid boundary) {
            super(boundary);
            setType(RoomType.PARKOUR);
        }

        @Override
        public void onPlayerEnter(Player player) {


        }

        @Override
        public void resetRoom() {

        }

        @Override
        public void update() {

        }
    }
    protected class CollectionRoom extends DungeonRoom {
        public CollectionRoom(Cuboid boundary) {
            super(boundary);
            setType(RoomType.COLLECTION);
        }
        @Override
        public void onPlayerEnter(Player player) {


        }
        @Override
        public void resetRoom() {

        }
        @Override
        public void update() {

        }
    }
    protected class TargetRoom extends SpawnerDungeonRoom {

        public ArrayList<Block> targetBlocks;

        public TargetRoom(Cuboid boundary) {
            super(boundary);
            setType(RoomType.TARGET);
            targetBlocks = new ArrayList<>();
            getTargetBlocks();
            openEntrance();
            openExit();
        }

        @Override
        public void onPlayerEnter(Player player) {
            spawnMobs();

        }

        @Override
        public void resetRoom() {
            for (Block block : targetBlocks)
                block.setType(Material.NETHER_WART_BLOCK);

            openEntrance();
            openExit();
        }



        public void getTargetBlocks() {
            for (Block block : getBoundary()) {
                if (block.getType().equals(Material.NETHER_WART_BLOCK)) {
                    targetBlocks.add(block);
                }
            }
            Bukkit.getConsoleSender().sendMessage("Found " + targetBlocks.size() + " blocks");
        }

        public boolean checkTargets() {
            int count = 0;
            for (Block block : targetBlocks) {
                if (block.getType().equals(Material.REDSTONE_BLOCK)) {
                    ++count;
                }
            }
            Bukkit.getConsoleSender().sendMessage(count + "/" + targetBlocks.size() + " targets hit");

            if (count == targetBlocks.size())
                return true;

            return false;
        }

        public void onHitAllTargets() {
            for (Block b : targetBlocks) {
                b.setType(Material.NETHER_WART_BLOCK);
            }
            targetBlocks.clear();

            openExit();
            dungeonManager.sendPlayersMessage(ChatColor.GREEN + "An entrance has opened!");

        }

        @Override
        public void update() {
            if (!targetBlocks.isEmpty() && checkTargets()) {
                onHitAllTargets();
            }
            super.update ();
        }
    }
    protected class MobRoom extends SpawnerDungeonRoom {


        public MobRoom(Cuboid boundary) {
            super(boundary);
            setType(RoomType.MOB);

            waveCount = 1;
            timeBetweenWaves = 0;

            openEntrance();
            openExit();
        }

        @Override
        public void onPlayerEnter(Player player) {
            if (!hasSpawned())
                spawnMobs();
        }

        @Override
        public void resetRoom() {
            openEntrance();
            openExit();
        }
    }
    protected class WaveRoom extends SpawnerDungeonRoom {



        public WaveRoom(Cuboid boundary) {
            super(boundary);
            setType(RoomType.WAVE);
            waveCount = 5;
            timeBetweenWaves = 8;

            openEntrance();
            openExit();
        }

        @Override
        public void onPlayerEnter(Player player) {
            closeEntrance();
            closeExit();
        }

        @Override
        public void resetRoom() {
            openEntrance();
            openExit();
        }

        @Override
        public void update() {
            super.update();

            if (currentWave != waveCount) {
                if (countdown <= 0) {
                    spawnNextWave();
                    countdown = timeBetweenWaves;
                }
                else {
                    --countdown;
                }
                return;
            }
            openExit();
        }

        public void spawnNextWave() {
            //This could be different if the mobs are different each wave.
            spawnMobs();
            ++currentWave;
        }

    }
    protected class BossRoom extends SpawnerDungeonRoom {

        private Cuboid bossTrigger;
        private Entity boss;
        private boolean bossSpawned = false;
        public BossRoom(Cuboid boundary, Cuboid bossTrigger) {
            super(boundary);
            setType(RoomType.BOSS);
            this.bossTrigger = bossTrigger;

            openEntrance();
            openExit();
        }

        public void playerTriggerEnter() {
            closeEntrance();
            closeExit();
            //spawnBoss ();
        }

        public Location findBossTrigger () {
            for (Block b : getBoundary()) {
                if (b.getType().equals(Material.MELON)) {
                    return b.getLocation();
                }
            }
            return null;
        }

        public void onBossKill () {

        }
        @Override
        public void onPlayerEnter(Player player) {

        }
        public void onBossTriggerEnter (Player p) {
            spawnBoss ();
            bossSpawned = true;
        }

        @Override
        public void resetRoom() {
            openEntrance();
            openExit();
        }

        public void spawnBoss () {
            boss = DungeonMobs.spawnKnightBoss (bossTrigger.getCenter());
        }

        @Override
        public void update() {
            super.update();
            if (!bossSpawned) {
                for (Player p : dungeonManager.getAlivePlayers()) {
                    if (bossTrigger.contains(p.getLocation())) {
                        onBossTriggerEnter(p);
                        return;
                    }
                }
            }
            if (boss == null)
                return;

            if (boss.isDead()) {
                openExit();
                onBossKill ();
                //Run end game code
            }
        }


    }
    protected class FinalRoom extends DungeonRoom {

        private Cuboid endDungeonTrigger;



        public FinalRoom(Cuboid boundary, Cuboid endDungeonTrigger) {
            super(boundary);
            setType(RoomType.FINAL);
            this.endDungeonTrigger = endDungeonTrigger;
        }

        public void playerTriggerEnter() {

        }

        @Override
        public void onPlayerEnter(Player player) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("DUNGEON COMPLETED"));

        }
        public void onEndTriggerEnter (Player p) {

        }

        @Override
        public void resetRoom() {

        }

        @Override
        public void update() {
            super.update();

            for (Player p : dungeonManager.getAlivePlayers()) {
                if (endDungeonTrigger.contains(p.getLocation())) {
                    onEndTriggerEnter(p);
                }
            }
        }
    }
}