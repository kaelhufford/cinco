package com.stigglespatch.main;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionListener implements Listener {
    private Main main;
    private CustomPlayer customPlayer;

    public ConnectionListener(Main main){
     this.main = main;
    }


    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        try {
            CustomPlayer playerData = new CustomPlayer(main, player);
            main.getPlayerManager().addCustomPlayer(player.getUniqueId(), playerData);

        } catch (SQLException ex) {
            //If we get an error about PlayerQuitEvent, we need to make sure that playerLeave () knows that the player
            //got kicked. We need to exit the function early.
            player.kickPlayer("The data that you have requested could not be gotten. Please contact administration.");
            ex.printStackTrace();
        }

    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent e) {

        customPlayer = main.getPlayerManager().getCustomPlayer(e.getPlayer().getUniqueId());

        if (customPlayer == null)
            return;


        Player p = e.getPlayer();
        String w = e.getPlayer().getWorld().getName();
        int x = Math.round((int) e.getPlayer().getLocation().getX());
        int y = Math.round((int) e.getPlayer().getLocation().getY());
        int z = Math.round((int) e.getPlayer().getLocation().getZ());


        customPlayer.setLogOffWorld(w);
        customPlayer.setLogOffX(x);
        customPlayer.setLogOffY(y);
        customPlayer.setLogOffZ(z);

        main.getPlayerManager().removeCustomPlayer(e.getPlayer().getUniqueId());


    }
}
