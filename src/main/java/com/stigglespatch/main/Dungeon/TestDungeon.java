package com.stigglespatch.main.Dungeon;

import com.stigglespatch.main.Dungeon.Cuboids.Cuboid;
import com.stigglespatch.main.Main;
import jdk.incubator.vector.VectorOperators;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;

public class TestDungeon extends Dungeon {

    String name;
    public TestDungeon(Main main, int dungeonID, Location worldSpawn) {
        super(main, dungeonID, new Location (Bukkit.getWorld ("testdungeon"), 43.5, -42, 190.5));
    }

    public void addRooms () {
        rooms.add (new LobbyRoom (new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),12, -35, 195),
                new Location(Bukkit.getWorld("testdungeon"),25, -45, 184)
        ), new Location(Bukkit.getWorld(name), 43.5, -42, 190.5)));

        rooms.add (new MobRoom (new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),12, -35, 195),
                new Location(Bukkit.getWorld("testdungeon"),25, -45, 184)
        ), new Location(Bukkit.getWorld(name), 9.5, -44, 190.5)));

        rooms.add (new FillerRoom (new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),11, -36, 182),
                new Location(Bukkit.getWorld("testdungeon"),-5, -45, 199)
        ), new Location(Bukkit.getWorld(name), 0.5, -44, 184.5)));

        rooms.add (new TargetRoom (new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),24,-31,181),
                new Location(Bukkit.getWorld("testdungeon"),-4,-53,149)
        ), new Location(Bukkit.getWorld(name), 4.5, -44, 179.5)));

        rooms.add (new FillerRoom (new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),-3,-39,125),
                new Location(Bukkit.getWorld("testdungeon"),11,-59,149)
        ), new Location(Bukkit.getWorld(name), 4.5, -57, 135.5)));

        rooms.add (new WaveRoom (new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),11,-48,124),
                new Location(Bukkit.getWorld("testdungeon"),-16,-58,111)
        ), new Location(Bukkit.getWorld(name), 4.5, -57, 122.5)));

        rooms.add (new MobRoom (new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),12,-49,110),
                new Location(Bukkit.getWorld("testdungeon"),28,-58,127)
        ), new Location(Bukkit.getWorld(name), 12.5, -57, 118.5)));

        rooms.add (new MobRoom (new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),27,-49,93),
                new Location(Bukkit.getWorld("testdungeon"),10,-58,110)
        ), new Location(Bukkit.getWorld(name), 19.5, -57, 107.5)));

        rooms.add (new MobRoom (new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),62,-23,115),
                new Location(Bukkit.getWorld("testdungeon"),28,-59,88)
        ), new Location(Bukkit.getWorld(name), 30.5, -57, 101.5)));

        rooms.add (new MobRoom (new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),63,-54,107),
                new Location(Bukkit.getWorld("testdungeon"),109,-27,96)
        ), new Location(Bukkit.getWorld(name), 65.5, -53, 101.5)));

        /*
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

         */


    }
}
