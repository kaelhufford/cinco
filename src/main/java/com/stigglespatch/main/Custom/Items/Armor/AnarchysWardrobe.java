package com.stigglespatch.main.Custom.Items.Armor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AnarchysWardrobe implements Listener {

    private static final ItemStack HELMET = createItem(Material.LEATHER_HELMET,"Anarchy's Helmet", "a_helmet");

    private static final ItemStack CHESTPLATE = createItem(Material.LEATHER_CHESTPLATE, "Anarchy's Chestplate", "a_chestplate");
    private static final ItemStack LEGGINGS = createItem(Material.LEATHER_LEGGINGS,  "Anarchy's Leggings", "a_leggings");
    private static final ItemStack BOOTS = createItem(Material.LEATHER_BOOTS,  "Anarchy's Boots", "a_boots");

    /*
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (isFullSetEquipped(player)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, true));
        }
    }
     */

    /*
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            Player defender = (Player) event.getEntity();
            if (isFullSetEquipped(attacker) && isFullSetEquipped(defender)) {
                event.setDamage(event.getDamage() + 1);
            }
        }
    }
    */

    /*
    private boolean isFullSetEquipped(Player player) {
        return player.getInventory().getHelmet() != null && player.getInventory().getHelmet().getItemMeta().equals(HELMET.getItemMeta()) &&
                player.getInventory().getChestplate() != null && player.getInventory().getChestplate().getItemMeta().equals(CHESTPLATE.getItemMeta()) &&
                player.getInventory().getLeggings() != null && player.getInventory().getLeggings().getItemMeta().equals(LEGGINGS.getItemMeta()) &&
                player.getInventory().getBoots() != null && player.getInventory().getBoots().getItemMeta().equals(BOOTS.getItemMeta());
    }
     */

    private static ItemStack createItem(Material material, String name, String localizedName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (item.equals(Material.LEATHER_BOOTS) || item.equals(Material.LEATHER_CHESTPLATE) || item.equals(Material.LEATHER_LEGGINGS) || item.equals(Material.LEATHER_HELMET)) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) item.getItemMeta();
            leatherArmorMeta.setDisplayName(name);
            leatherArmorMeta.setLocalizedName(localizedName);

        }

        return item;
    }

    public void getItems(Player p){
        p.getInventory().addItem(HELMET, CHESTPLATE, LEGGINGS, BOOTS);
    }

}

