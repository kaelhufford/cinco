package com.stigglespatch.main;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;


public final class Main extends JavaPlugin implements Listener {
    public static ArrayList roomsFinished = new ArrayList<Cuboid>();
    public static boolean closedBossEntry = false;


    private Database database;
    public static int roomNumber = 0;

    public int getRoomNumber(){
        return roomNumber;
    }
    DungeonStartCommand dSC = new DungeonStartCommand();
    private PlayerManager playerManager;


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

        Cuboid entryCuboid = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),12, -35, 195),
                new Location(Bukkit.getWorld("testdungeon"),25, -45, 184));
        Cuboid fillerRoomMine = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),11, -36, 182),
                new Location(Bukkit.getWorld("testdungeon"),-5, -45, 199));
        Cuboid targetRoom = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),24,-31,181),
                new Location(Bukkit.getWorld("testdungeon"),-4,-53,149));
        Cuboid fillerRoomDrop = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),-3,-39,125),
                new Location(Bukkit.getWorld("testdungeon"),11,-59,149));
        Cuboid collectionRoom = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),11,-48,124),
                new Location(Bukkit.getWorld("testdungeon"),-16,-58,111));
        Cuboid mobRoomNormal = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),12,-49,110),
                new Location(Bukkit.getWorld("testdungeon"),28,-58,127));
        Cuboid mobRoomLava = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),27,-49,93),
                new Location(Bukkit.getWorld("testdungeon"),10,-58,110));
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
                            dSC.getPlayersList().clear();
                            dSC.getAlivePlayers().clear();

                        }
                    } else {
                        return;
                    }
                }
        },0, 5);
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player p : dSC.getAlivePlayers()){
                int x = (int) p.getLocation().getX();
                int y = (int) p.getLocation().getY();
                int z = (int) p.getLocation().getZ();
                String world = p.getWorld().getName();

                p.getLocation().getWorld().spawnEntity(new Location(Bukkit.getWorld(world), x,y,z), EntityType.ZOMBIE);
            }
        },0, 100);
    }
    @Override
    public void onDisable(){
        database.disconnect();
        dSC.getPlayersList().clear();
        dSC.getAlivePlayers().clear();
    }


    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (dSC.getPlayersList().contains(p)){
            dSC.getPlayersList().remove(p);
        }
        if (dSC.getAlivePlayers().contains(p)){
            dSC.getAlivePlayers().remove(p);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
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
        }


    }

    public Database getDatabase() { return database; }
    public PlayerManager getPlayerManager() { return playerManager; }

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
