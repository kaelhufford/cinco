package com.stigglespatch.main;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Dungeon {
    private int dungeonID;
    private Location spawn;
    private DungeonState state;

    private List<UUID> players;

    public Dungeon(int dungeonID, Location spawn){
        this.dungeonID = dungeonID;
        this.spawn = spawn;

        this.state = DungeonState.RECRUITING;
        this.players = new ArrayList<>();
    }
}
