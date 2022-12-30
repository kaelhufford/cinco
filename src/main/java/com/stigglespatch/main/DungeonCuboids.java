package com.stigglespatch.main;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class DungeonCuboids {



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
            new Location(Bukkit.getWorld("testdungeon"),109,-27,107));



}
