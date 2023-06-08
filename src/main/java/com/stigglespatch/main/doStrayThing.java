package com.stigglespatch.main;

import com.stigglespatch.main.Custom.Entities.Entities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class doStrayThing implements CommandExecutor {

    private Main main;
    public doStrayThing(Main main){
        this.main = main;
    }
    Entities entities = new Entities();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player){
            Player p = (Player) sender;
            if (p.isOp()){
                //main.spawnStrayGroup();
                main.spawnTheBeast();
            }
        }
        return false;
    }
}
