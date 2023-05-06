package com.stigglespatch.main.Custom.Items.Armor;

import com.stigglespatch.main.Custom.Items.Bows.BoomBow;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class AnarchysWardrobe implements Listener {

    private ItemStack getAnarchysHelmet(){
        ItemStack item = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setUnbreakable(true);
        meta.setColor(Color.fromRGB(166, 0, 199));
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Anarchy's Helmet");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY +  "",
                ChatColor.LIGHT_PURPLE +  "-- SPECIAL ARMOR --",
                ChatColor.LIGHT_PURPLE + "- ANARCHY'S WARDROBE -",
                ChatColor.GRAY + ChatColor.BOLD.toString() + "USELSS BY ITSELF",
                ChatColor.GRAY + "When paired with the full",
                ChatColor.GRAY + "set of ANARCHY'S WARDROBE you",
                ChatColor.GRAY + "gain the following buffs",
                ChatColor.GRAY + ChatColor.BOLD.toString() + "IN COMBAT",
                ChatColor.GRAY + "- Regeneration 1",
                ChatColor.GRAY + "- Speed 1"));
        meta.setLocalizedName("a_helmet");
        item.setItemMeta(meta);
        return item;
    }
    private ItemStack getAnarchysChestplate(){
        ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setUnbreakable(true);
        meta.setColor(Color.fromRGB(166, 0, 199));
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Anarchy's Chestplate");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY +  "",
                ChatColor.LIGHT_PURPLE +  "-- SPECIAL ARMOR --",
                ChatColor.LIGHT_PURPLE + "- ANARCHY'S WARDROBE -",
                ChatColor.GRAY + ChatColor.BOLD.toString() + "USELSS BY ITSELF",
                ChatColor.GRAY + "When paired with the full",
                ChatColor.GRAY + "set of ANARCHY'S WARDROBE you",
                ChatColor.GRAY + "gain the following buffs",
                ChatColor.GRAY + ChatColor.BOLD.toString() + "IN COMBAT",
                ChatColor.GRAY + "- Regeneration 1",
                ChatColor.GRAY + "- Speed 1"));
        meta.setLocalizedName("a_chestplate");
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getAnarchysLeggings(){
        ItemStack item = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setUnbreakable(true);
        meta.setColor(Color.fromRGB(166, 0, 199));
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Anarchy's Leggings");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY +  "",
                ChatColor.LIGHT_PURPLE +  "-- SPECIAL ARMOR --",
                ChatColor.LIGHT_PURPLE + "- ANARCHY'S WARDROBE -",
                ChatColor.GRAY + ChatColor.BOLD.toString() + "USELSS BY ITSELF",
                ChatColor.GRAY + "When paired with the full",
                ChatColor.GRAY + "set of ANARCHY'S WARDROBE you",
                ChatColor.GRAY + "gain the following buffs",
                ChatColor.GRAY + ChatColor.BOLD.toString() + "IN COMBAT",
                ChatColor.GRAY + "- Regeneration 1",
                ChatColor.GRAY + "- Speed 1"));
        meta.setLocalizedName("a_leggins");
        item.setItemMeta(meta);
        return item;
    }
    private ItemStack getAnarchysBoots(){
        ItemStack item = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setUnbreakable(true);
        meta.setColor(Color.fromRGB(166, 0, 199));
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Anarchy's Boots");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY +  "",
                ChatColor.LIGHT_PURPLE +  "-- SPECIAL ARMOR --",
                ChatColor.LIGHT_PURPLE + "- ANARCHY'S WARDROBE -",
                ChatColor.GRAY + ChatColor.BOLD.toString() + "USELSS BY ITSELF",
                ChatColor.GRAY + "When paired with the full",
                ChatColor.GRAY + "set of ANARCHY'S WARDROBE you",
                ChatColor.GRAY + "gain the following buffs",
                ChatColor.GRAY + ChatColor.BOLD.toString() + "IN COMBAT",
                ChatColor.GRAY + "- Regeneration 1",
                ChatColor.GRAY + "- Speed 1"));
        meta.setLocalizedName("a_boots");
        item.setItemMeta(meta);
        return item;
    }

    private Boolean isAnarchySet(Player p){
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

    /*
SET: Anarchy's Wardrobe
Helmet: Anarchy's Helmet
USELESS BY ITSELF
When paired with the full set of Anarchy's Wardrobe, this along with the full set will provide the following effects while in combat:
Regeneration I
Slight Speed Increase

Chestplate: Anarchy's Chestplate
USELESS BY ITSELF
When paired with the full set of Anarchy's Wardrobe, this along with the full set will provide the following effects while in combat:
Regeneration I
Slight Speed Increase

Leggings: Anarchy's Leggings
USELESS BY ITSELF
When paired with the full set of Anarchy's Wardrobe, this along with the full set will provide the following effects while in combat:
Regeneration I
Slight Speed Increase

Boots: Anarchy's Boots
USELESS BY ITSELF
When paired with the full set of Anarchy's Wardrobe, this along with the full set will provide the following effects while in combat:
Regeneration I
Slight Speed Increase
     */

    @EventHandler
    public void playerInCombat(PlayerInteractEvent e){
        Player p = e.getPlayer();
        System.out.println("Checking for full set.");

        if (isAnarchySet(p)){
            System.out.println("Has the full set.");

            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5, 1, true, true, true));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5, 1, true, true, true));
        }
    }


    public void getItems(Player p){
        p.getInventory().addItem(getAnarchysHelmet(), getAnarchysChestplate(), getAnarchysLeggings(), getAnarchysBoots());
    }

}

