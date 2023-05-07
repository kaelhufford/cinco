package com.stigglespatch.main.Custom.Items.Armor;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class PeacesSymphony implements Listener {

    private ItemStack getPeaceHelmet(){
        ItemStack item = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setUnbreakable(true);
        meta.setColor(Color.fromRGB(102, 255, 255));
        meta.setDisplayName(ChatColor.AQUA + "The Symphony's Helmet");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY +  "",
                ChatColor.AQUA +  "-- SPECIAL ARMOR --",
                ChatColor.AQUA + "-= PEACES SYMPHONY =-",
                ChatColor.GRAY + ChatColor.BOLD.toString() + "USELSS BY ITSELF",
                ChatColor.GRAY + "When paired with the full",
                ChatColor.GRAY + "set of PEACES SYMPHONY you",
                ChatColor.GRAY + "gain the following buffs",
                ChatColor.GRAY + ChatColor.BOLD.toString() + "NOT IN COMBAT",
                ChatColor.GRAY + "- Haste I",
                ChatColor.GRAY + "- Speed I",
                ChatColor.GRAY + "- Regeneration I"));
        meta.setLocalizedName("symp_helmet");
        item.setItemMeta(meta);
        return item;
    }
    private ItemStack getPeaceChestplate(){
        ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setUnbreakable(true);
        meta.setColor(Color.fromRGB(102, 255, 255));
        meta.setDisplayName(ChatColor.AQUA + "The Symphony's Chestplate");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY +  "",
                ChatColor.AQUA +  "-- SPECIAL ARMOR --",
                ChatColor.AQUA + "-= PEACES SYMPHONY =-",
                ChatColor.GRAY + ChatColor.BOLD.toString() + "USELSS BY ITSELF",
                ChatColor.GRAY + "When paired with the full",
                ChatColor.GRAY + "set of PEACES SYMPHONY you",
                ChatColor.GRAY + "gain the following buffs",
                ChatColor.GRAY + ChatColor.BOLD.toString() + "NOT IN COMBAT",
                ChatColor.GRAY + "- Haste I",
                ChatColor.GRAY + "- Speed I",
                ChatColor.GRAY + "- Regeneration I"));
        meta.setLocalizedName("symp_chestplate");
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getPeaceLeggings(){
        ItemStack item = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setUnbreakable(true);
        meta.setColor(Color.fromRGB(102, 255, 255));
        meta.setDisplayName(ChatColor.AQUA + "The Symphony's Leggings");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY +  "",
                ChatColor.AQUA +  "-- SPECIAL ARMOR --",
                ChatColor.AQUA + "-= PEACES SYMPHONY =-",
                ChatColor.GRAY + ChatColor.BOLD.toString() + "USELSS BY ITSELF",
                ChatColor.GRAY + "When paired with the full",
                ChatColor.GRAY + "set of PEACES SYMPHONY you",
                ChatColor.GRAY + "gain the following buffs",
                ChatColor.GRAY + ChatColor.BOLD.toString() + "NOT IN COMBAT",
                ChatColor.GRAY + "- Haste I",
                ChatColor.GRAY + "- Speed I",
                ChatColor.GRAY + "- Regeneration I"));
        meta.setLocalizedName("symp_leggins");
        item.setItemMeta(meta);
        return item;
    }
    private ItemStack getPeaceBoots(){
        ItemStack item = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setUnbreakable(true);
        meta.setColor(Color.fromRGB(102, 255, 255));
        meta.setDisplayName(ChatColor.AQUA + "The Symphony's Boots");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY +  "",
                ChatColor.AQUA +  "-- SPECIAL ARMOR --",
                ChatColor.AQUA + "-= PEACES SYMPHONY =-",
                ChatColor.GRAY + ChatColor.BOLD.toString() + "USELSS BY ITSELF",
                ChatColor.GRAY + "When paired with the full",
                ChatColor.GRAY + "set of PEACES SYMPHONY you",
                ChatColor.GRAY + "gain the following buffs",
                ChatColor.GRAY + ChatColor.BOLD.toString() + "NOT IN COMBAT",
                ChatColor.GRAY + "- Haste I",
                ChatColor.GRAY + "- Speed I",
                ChatColor.GRAY + "- Regeneration I"));
        meta.setLocalizedName("symp_boots");
        item.setItemMeta(meta);
        return item;
    }

    private Boolean isPeaceSet(Player p){
        System.out.println("Checking for full set.");

        String helm = p.getInventory().getHelmet().getItemMeta().getLocalizedName();
        String chest = p.getInventory().getChestplate().getItemMeta().getLocalizedName();
        String legs = p.getInventory().getLeggings().getItemMeta().getLocalizedName();
        String boot = p.getInventory().getBoots().getItemMeta().getLocalizedName();

        if (helm.equals("a_helmet") && chest.equals("a_chestplate") && legs.equals("a_leggins") && boot.equals("a_boots")){
            return true;
        }
        return false;
    }


    @EventHandler
    public void playerInCombat(PlayerInteractEvent e){
        Player p = e.getPlayer();

        if (isPeaceSet(p)){

            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 1, true, true, true));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 1, true, true, true));
        }
    }


    public void getItems(Player p){
        p.getInventory().addItem(getPeaceHelmet(), getPeaceChestplate(), getPeaceLeggings(), getPeaceBoots());
    }

}

