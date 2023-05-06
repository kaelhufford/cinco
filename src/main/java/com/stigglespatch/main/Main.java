package com.stigglespatch.main;

import com.stigglespatch.main.Custom.Enchants.Smelter;
import com.stigglespatch.main.Custom.Items.Bows.BoomBow;
import com.stigglespatch.main.Custom.Items.Bows.GlowBow;
import com.stigglespatch.main.Custom.Items.Swords;
import com.stigglespatch.main.Database.ConnectionListener;
import com.stigglespatch.main.Database.Database;
import com.stigglespatch.main.Database.PlayerManager;
import com.stigglespatch.main.Dungeon.*;
import com.stigglespatch.main.Dungeon.Cuboids.Cuboid;
import com.stigglespatch.main.Misc.CreativeCommand;
import com.stigglespatch.main.Misc.SMPCommand;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;


public final class Main extends JavaPlugin implements Listener {
    public static ArrayList roomsFinished = new ArrayList<Cuboid>();
    public static ArrayList zombieSpawnRooms = new ArrayList<Cuboid>();
    public static boolean closedBossEntry = false;


    private Database database;
    public static int roomNumber = 0;

    public int getRoomNumber(){
        return roomNumber;
    }
    DungeonStartCommand dSC = new DungeonStartCommand();
    //DungeonMobs dungeonMobs = new DungeonMobs();
    private PlayerManager playerManager;
    public final NamespacedKey pendant = new NamespacedKey(this, "pendant");


    DungeonManager dm = new DungeonManager(this);

    @Override
    public void onEnable() {
        database = new Database();
        try {
            database.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        playerManager = new PlayerManager();

        Bukkit.getPluginCommand("dungeon").setExecutor(new DungeonCommand(this));
        Bukkit.getPluginCommand("smp").setExecutor(new SMPCommand());
        Bukkit.getPluginCommand("creative").setExecutor(new CreativeCommand());
        Bukkit.getPluginCommand("start-dungeon").setExecutor(new DungeonStartCommand());
        Bukkit.getPluginCommand("check").setExecutor(new CheckCommand());

        if (Bukkit.getWorld("smp_cinco") == null){
            Bukkit.getServer().createWorld(new WorldCreator("smp_cinco"));
        }
        if (Bukkit.getWorld("testdungeon") == null){
            Bukkit.getServer().createWorld(new WorldCreator("testdungeon"));
        }
        System.out.println("Generated the third-party worlds.");

        Bukkit.getPluginManager().registerEvents(new ConnectionListener(this), this);
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new DungeonStartCommand(), this);
        Bukkit.getPluginManager().registerEvents(new DungeonMobs(), this);

        Bukkit.getPluginManager().registerEvents(new Swords(), this);
        Bukkit.getPluginManager().registerEvents(new BoomBow(), this);
        Bukkit.getPluginManager().registerEvents(new GlowBow(), this);

        Smelter as = new Smelter();
        Bukkit.getPluginManager().registerEvents(as, this);
        registerEnchantment(as);
    }

    private void registerEnchantment(Enchantment enchantment){
        try {
            Field field =Enchantment.class.getDeclaredField("acceptingNew");
            field.setAccessible(true);
            field.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (NoSuchFieldException | IllegalAccessException e){
            throw new RuntimeException(e);
        }
    }
    private void getEntitiesInRoom(Cuboid collectionRoom) {
        for(Block block : collectionRoom.getBlocks()){
            if(block.getType().equals(Material.LIGHT_GRAY_CANDLE)){
                DungeonMobs.spawnDungeonZombie(block.getLocation().add(0,1,0));
            } else if (block.getType().equals(Material.GRAY_CANDLE)){
                DungeonMobs.spawnDungeonSkeleton(block.getLocation().add(0,1,0));
            } else if (block.getType().equals(Material.CYAN_CANDLE)){
                DungeonMobs.spawnDungeonCreeper(block.getLocation().add(0,1,0));
            }
        }
    }

    @Override
    public void onDisable(){
        database.disconnect();
        //dSC.getPlayersList().clear();
        //dSC.getAlivePlayers().clear();
        //roomsFinished.clear();
    }

    @EventHandler
    public void onBoomBoom(BlockExplodeEvent e){
        if(e.getBlock().getWorld().equals("testdungeon")){
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        /*Player p = e.getPlayer();
        if (dSC.getPlayersList().contains(p)){
            dSC.getPlayersList().remove(p);
        }
        if (dSC.getAlivePlayers().contains(p)){
            dSC.getAlivePlayers().remove(p);
        }*/
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        /*
        Player p = (Player) e.getEntity();
        if (dSC.getPlayersList().contains(p)){
            p.setAllowFlight(true);
            p.setFlying(true);
            p.setInvisible(true);
        }
        if (dSC.getAlivePlayers().contains(p)){
            dSC.getAlivePlayers().remove(p);
        }

        if (dSC.getAlivePlayers().size() == 0){
            Bukkit.broadcastMessage(ChatColor.YELLOW +"The dungeon has been reset! Do /start-dungeon to begin!");
            roomsFinished.clear();
            for (Player player : dSC.getPlayersList()) {
                player.setGameMode(GameMode.ADVENTURE);
                player.setInvisible(false);
                player.setAllowFlight(false);
                player.setFlying(false);
            }
            dSC.getPlayersList().clear();
            dSC.getAlivePlayers().clear();
        }*/


    }

    public Database getDatabase() { return database; }
    public PlayerManager getPlayerManager() { return playerManager; }
    public NamespacedKey getNamespacedKey() { return pendant; }

}

/* INSERT INTO DATABASE
    PreparedStatement ps = database.getConnection().prepareStatement("INSERT INTO table (C1,C2,C4) VALUES (?,?,?);"); //insert
            ps.setString(1,"banana");
                    ps.setInt(2,2);
                    ps.setBoolean(3,true);
                    ps.executeUpdate();


                    PreparedStatement ps2 = database.getConnection().prepareStatement("UPDATE table SET column = ? WHERE column2 = ?;"); //update
                    ps2.setString(1,"banana");
                    ps2.setInt(2,2);
                    ps.executeUpdate();
INSERT INTO DATABASE

DELETE FROM DATABASE
                    PreparedStatement ps3 = database.getConnection().prepareStatement("DELETE FROM table WHERE column =?"); //delete
                    ps3.setString(1,"banana");
                    ps.executeUpdate();
DELETE FROM DATABASE

RETRIEVE DATA FROM DATABASE
                    //Example: replace UUID with * or UUID, RANK (to get all UUID/RANK whom it applies to | or everything that relates to people whom it is true for.)
                    PreparedStatement ps4 = database.getConnection().prepareStatement("SELECT UUID FROM table WHERE column = ?"); //retrieve data from all people which the condition applies to
                    ResultSet rs = ps4.executeQuery();
                    while (rs.next()) {
                    System.out.println(rs.getString("UUID"));
                    }
RETRIEVE DATA FROM DATABASE
 */
        /*
        Cuboid entryCuboid = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),12, -35, 195),
                new Location(Bukkit.getWorld("testdungeon"),25, -45, 184));
        Cuboid fillerRoomMine = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),11, -36, 182),
                new Location(Bukkit.getWorld("testdungeon"),-5, -45, 199));
        zombieSpawnRooms.add(fillerRoomMine);
        Cuboid targetRoom = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),24,-31,181),
                new Location(Bukkit.getWorld("testdungeon"),-4,-53,149));
        zombieSpawnRooms.add(targetRoom);
        Cuboid fillerRoomDrop = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),-3,-39,125),
                new Location(Bukkit.getWorld("testdungeon"),11,-59,149));
        zombieSpawnRooms.add(fillerRoomDrop);
        Cuboid collectionRoom = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),11,-48,124),
                new Location(Bukkit.getWorld("testdungeon"),-16,-58,111));
        zombieSpawnRooms.add(collectionRoom);
        Cuboid mobRoomNormal = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),12,-49,110),
                new Location(Bukkit.getWorld("testdungeon"),28,-58,127));
        zombieSpawnRooms.add(mobRoomNormal);
        Cuboid mobRoomLava = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),27,-49,93),
                new Location(Bukkit.getWorld("testdungeon"),10,-58,110));
        zombieSpawnRooms.add(mobRoomLava);
        Cuboid bossRoom = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),62,-23,115),
                new Location(Bukkit.getWorld("testdungeon"),28,-59,88));
        Cuboid finalRoom = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),63,-54,107),
                new Location(Bukkit.getWorld("testdungeon"),109,-27,96));
        Cuboid finalRoomTriggerWall = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),32,-50,94),
                new Location(Bukkit.getWorld("testdungeon"),35,-57,108));
        Cuboid finalRoomWall = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),28,-57,99),
                new Location(Bukkit.getWorld("testdungeon"),28,-54,103));
        Cuboid finalRoomTP = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),109,-49,97),
                new Location(Bukkit.getWorld("testdungeon"),90,-41,105));
        Cuboid tutorialDungeon = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),111, -64, 81),
                new Location(Bukkit.getWorld("testdungeon"),-20, -14, 211));


        Bukkit.getScheduler().runTaskTimer(this, () -> {
                for (Player p : dSC.getPlayersList()) {
                    if (entryCuboid.contains(p.getLocation())){
                        if (!roomsFinished.contains(entryCuboid)) {
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("⬛⬜⬜⬜⬜⬜⬜⬜⬜"));
                            p.sendMessage(ChatColor.RED +"[WARNING] By moving on the the next room, no one else will be able to join!");
                        } else {
                            p.teleport(p.getLocation().subtract(2,0,0));
                            p.sendMessage(ChatColor.RED + "You have already passed this room, you may not go back!");
                        }

                    } else if(fillerRoomMine.contains(p.getLocation())){
                        if (!roomsFinished.contains(fillerRoomMine)) {
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("⬛⬛⬜⬜⬜⬜⬜⬜⬜"));
                            if (dSC.getPlayersList().size() > 4){
                                dSC.getPlayersList().add(p);
                            }

                            roomsFinished.add(entryCuboid);
                        } else {
                            p.teleport(p.getLocation().subtract(0,0,2));
                            p.sendMessage(ChatColor.RED + "You have already passed this room, you may not go back!");
                        }

                    } else if(targetRoom.contains(p.getLocation())){
                        if (!roomsFinished.contains(targetRoom)) {
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("⬛⬛⬛⬜⬜⬜⬜⬜⬜"));
                            roomsFinished.add(fillerRoomMine);
                        } else {
                            p.teleport(p.getLocation().subtract(0,0,2));
                            p.sendMessage(ChatColor.RED + "You have already passed this room, you may not go back!");
                        }

                    } else if(fillerRoomDrop.contains(p.getLocation())){
                        if (!roomsFinished.contains(fillerRoomDrop)) {
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("⬛⬛⬛⬛⬜⬜⬜⬜⬜"));
                            roomsFinished.add(targetRoom);
                        } else {
                            p.teleport(p.getLocation().subtract(0,0,2));
                            p.sendMessage(ChatColor.RED + "You have already passed this room, you may not go back!");
                        }

                    } else if(collectionRoom.contains(p.getLocation())){
                        if (!roomsFinished.contains(collectionRoom)) {
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("⬛⬛⬛⬛⬛⬜⬜⬜⬜"));
                            roomsFinished.add(fillerRoomDrop);
                        } else {
                            p.teleport(p.getLocation().add(2,0,0));
                            p.sendMessage(ChatColor.RED + "You have already passed this room, you may not go back!");
                        }

                    } else if(mobRoomNormal.contains(p.getLocation())){
                        if (!roomsFinished.contains(mobRoomNormal)) {
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("⬛⬛⬛⬛⬛⬛⬜⬜⬜"));
                            roomsFinished.add(collectionRoom);
                        } else {
                            p.teleport(p.getLocation().subtract(0,0,2));
                            p.sendMessage(ChatColor.RED + "You have already passed this room, you may not go back!");
                        }

                    } else if(mobRoomLava.contains(p.getLocation())){
                        if (!roomsFinished.contains(mobRoomLava)) {
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("⬛⬛⬛⬛⬛⬛⬛⬜⬜"));
                            roomsFinished.add(mobRoomNormal);
                        } else {
                            p.teleport(p.getLocation().add(5,0,0));
                            p.sendMessage(ChatColor.RED + "You have already passed this room, you may not go back!");
                        }

                    } else if(bossRoom.contains(p.getLocation())){
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("⬛⬛⬛⬛⬛⬛⬛⬛⬜"));

                        if (finalRoomTriggerWall.contains(p.getLocation()) && closedBossEntry == false){
                            for (Block block : finalRoomWall.getBlocks()){
                                block.setType(Material.DEEPSLATE_TILE_WALL);
                            }
                            p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 1, (float) .5);
                            p.sendMessage(ChatColor.RED + "The door has locked! you must fight the boss to find your way out of this dungeon, best of luck!");
                            closedBossEntry = true;
                        }

                    } else if(finalRoom.contains(p.getLocation())){
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("DUNGEON COMPLETED"));
                        if (finalRoomTP.contains(p.getLocation())){
                            for (Block block : finalRoomWall.getBlocks()){
                                block.setType(Material.AIR);
                            }
                            p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 100, 1);
                            p.playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 100, 1);
                            p.teleport(Bukkit.getWorld("testdungeon").getBlockAt(158,-20,18).getLocation());
                            p.sendMessage(ChatColor.GREEN + "You have defeated the boss! Congratulations!");
                            closedBossEntry = false;
                            Bukkit.broadcastMessage(ChatColor.YELLOW +"The dungeon has been reset! Do /start-dungeon to begin!");
                            roomsFinished.clear();
                            for (Player player : dSC.getPlayersList()) {
                                player.setGameMode(GameMode.ADVENTURE);
                                player.setInvisible(false);
                                player.setAllowFlight(false);
                                player.setFlying(false);
                            }
                            for (Entity e : tutorialDungeon.getWorld().getEntities()){
                                if (tutorialDungeon.contains(e.getLocation())){
                                    e.remove();
                                }
                            }
                            dSC.getPlayersList().clear();
                            dSC.getAlivePlayers().clear();

                        }
                    } else {
                        return;
                    }
                }
        },0, 5);

        int min = 60;
        int max = 100;
        int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
        long timeBeforeNextSpawn = random_int;


        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player p : dSC.getAlivePlayers()){
                if(fillerRoomMine.contains(p.getLocation())){
                    getEntitiesInRoom(fillerRoomMine);

                } else if(targetRoom.contains(p.getLocation())){
                    getEntitiesInRoom(targetRoom);

                } else if(fillerRoomDrop.contains(p.getLocation())){
                    getEntitiesInRoom(fillerRoomDrop);

                } else if(collectionRoom.contains(p.getLocation())){
                    getEntitiesInRoom(collectionRoom);

                } else if(mobRoomNormal.contains(p.getLocation())){
                    getEntitiesInRoom(mobRoomNormal);

                } else if(mobRoomLava.contains(p.getLocation())){
                    getEntitiesInRoom(mobRoomLava);
                }
            }
        },0, timeBeforeNextSpawn);*/
