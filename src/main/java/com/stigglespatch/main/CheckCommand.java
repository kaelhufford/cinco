package com.stigglespatch.main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckCommand implements CommandExecutor {

    DungeonStartCommand dSC = new DungeonStartCommand();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            sender.sendMessage("Dungeon players array list amount: " + dSC.getPlayers().size() +".");
            sender.sendMessage("Dungeon alive array list amount: " + dSC.getAlivePlayers().size()+".");

        } else {
            System.out.println("Dungeon players array list amount: " + dSC.getPlayers().size() +".");
            System.out.println("Dungeon alive array list amount: " + dSC.getAlivePlayers().size()+".");
        }

        return false;
    }
}
