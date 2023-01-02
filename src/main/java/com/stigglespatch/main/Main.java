package com.stigglespatch.main;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.sql.SQLException;
import java.util.ArrayList;

public final class Main extends JavaPlugin implements Listener {

    private Database database;
    DungeonStartCommand dSC = new DungeonStartCommand();
    private Cuboid cuboid;
    private PlayerManager playerManager;
    private CustomPlayer customPlayer;

    @Override
    public void onEnable() {
        database = new Database();
        try {
            database.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        playerManager = new PlayerManager();

        Bukkit.getPluginCommand("dungeon").setExecutor(new DungeonCommand());
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

        Bukkit.getScheduler().runTaskTimer(this, () -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (entryCuboid.contains(p.getLocation())){
                        p.sendMessage("You are in the starting room.");

                    } else if(fillerRoomMine.contains(p.getLocation())){
                        p.sendMessage("You are in the first filler room.");

                    } else if(targetRoom.contains(p.getLocation())){
                        p.sendMessage("You are in the targeting room.");

                    } else if(fillerRoomDrop.contains(p.getLocation())){
                        p.sendMessage("You are in the filler drop room.");

                    } else if(collectionRoom.contains(p.getLocation())){
                        p.sendMessage("You are in the collection room.");

                    } else if(mobRoomNormal.contains(p.getLocation())){
                        p.sendMessage("You are in the normal mob room.");

                    } else if(mobRoomLava.contains(p.getLocation())){
                        p.sendMessage("You are in the lava mob room.");

                    } else if(bossRoom.contains(p.getLocation())){
                        p.sendMessage("You are in the boss room.");

                    } else if(finalRoom.contains(p.getLocation())){
                        p.sendMessage("You are in the final room.");

                    } else {
                        return;
                    }
                }
        },0, 20);
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
        if (dSC.getAlivePlayers().contains(p)){
            dSC.getAlivePlayers().remove(p);
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
