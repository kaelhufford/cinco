package com.stigglespatch.main.Dungeon;

import com.stigglespatch.main.Dungeon.Cuboids.Cuboid;
import org.bukkit.Location;

public class DungeonCuboid extends Cuboid {

    private Location spawnLocation;
    private boolean finished;

    public DungeonCuboid(Location l1, Location l2) {
        this (l1, l2, l1);

    }
    public DungeonCuboid(Location l1, Location l2, Location spawn) {
        super (l1, l2);
        spawnLocation = spawn;
        finished = false;
    }

    public void setSpawnLocation (Location l) {
        spawnLocation = l;
    }

    public Location getSpawnLocation () {
        return spawnLocation;
    }

    public boolean isFinished () { return finished; }

    public void setFinished (boolean val) {
        finished = val;
    }

}
