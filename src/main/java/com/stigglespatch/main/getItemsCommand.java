package com.stigglespatch.main;

import com.stigglespatch.main.Custom.Items.Armor.AnarchysWardrobe;
import com.stigglespatch.main.Custom.Items.Armor.PeacesSymphony;
import com.stigglespatch.main.Custom.Items.Bows.BoomBow;
import com.stigglespatch.main.Custom.Items.Bows.GlowBow;
import com.stigglespatch.main.Custom.Items.Pickaxes;
import com.stigglespatch.main.Custom.Items.Swords;
import com.stigglespatch.main.Dungeon.DungeonStartCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class getItemsCommand implements CommandExecutor {
    Pickaxes pickaxes = new Pickaxes();
    Swords swords = new Swords();
    BoomBow boomBow = new BoomBow();
    GlowBow glowBow = new GlowBow();
    AnarchysWardrobe anarchysWardrobe = new AnarchysWardrobe();
    PeacesSymphony peacesSymphony = new PeacesSymphony();


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){
            Player p = (Player) sender;
            p.getInventory().addItem(pickaxes.giveHandyToolPickaxe());
            p.getInventory().addItem(pickaxes.giveWardenWeaknessPickaxe());
            p.getInventory().addItem(swords.getTheEmeraldDagger());
            p.getInventory().addItem(swords.getTheMagmaCutlass());
            p.getInventory().addItem(boomBow.getBoomBowPlayer(p));
            p.getInventory().addItem(glowBow.getGlowBowPlayer());
            peacesSymphony.getItems(p);
            anarchysWardrobe.getItems(p);
        }


        return false;
    }
}
