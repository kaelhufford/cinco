package com.stigglespatch.main.Custom.Items.Bows;

import com.stigglespatch.main.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class GlowBow implements Listener {

    private static final Main plugin = Main.getPlugin(Main.class);
    private static final NamespacedKey glowKey = new NamespacedKey(plugin, "glow_arrow");


    private ItemStack getGlowBow(){
        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta meta = bow.getItemMeta();
        meta.setUnbreakable(true);
        meta.setDisplayName(("Recon Bow"));
        meta.setLore(Arrays.asList("This bow scans nearby entities within a "));
        meta.setLocalizedName("glow_bow");
        bow.setItemMeta(meta);
        return bow;
    }
    @EventHandler
    public void onShoot(EntityShootBowEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        if (e.getBow().getItemMeta().getLocalizedName().equals("glow_bow")){
            e.getProjectile().getPersistentDataContainer().set(glowKey, PersistentDataType.STRING, "boom_arrow");
        }
    }

    @EventHandler
    public void onDamage(ProjectileHitEvent e){
        if (e.getHitEntity() == null) { return; }
        PersistentDataContainer container = e.getEntity().getPersistentDataContainer();
        if (!container.has(glowKey, PersistentDataType.STRING)) return;
        if (container.get(glowKey, PersistentDataType.STRING).equals("glow_bow")) {
            for (Entity entity : e.getEntity().getNearbyEntities(10,10,10)){
                if (entity instanceof Mob){
                    ((Mob) entity).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 10, 10, true, false, true));
                }
            }
        }
    }
    public ItemStack getGlowBowPlayer(){
        return getGlowBow();
    }

}
