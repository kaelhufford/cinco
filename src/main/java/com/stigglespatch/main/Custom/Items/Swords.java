package com.stigglespatch.main.Custom.Items;

import com.stigglespatch.main.Main;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Random;

public class Swords implements Listener {

    private static final Main plugin = Main.getPlugin(Main.class);
    private static final NamespacedKey fireKey = new NamespacedKey(plugin, "magama_arrow_key");

    private int rollNumber(int min, int max){
        Random rand = new Random();
        int randomNumber = rand.nextInt(max) + min;

        return randomNumber;
    }

    private ItemStack getEmeraldDagger(){
        /*
        Emerald Dagger (Emerald)
        Has a 10% chance to drop 1-3 Emeralds on a kill.
        When paired with Sharpness V, the dagger has a 20% chance to deal 50% more damage.

        Magma Cutlass (Magma Cream)
        When right-clicked, if not on the 15-second cooldown, will send a flaming arrow in the direction you are facing.
        When taking fire damage, the cutlass's damage will deal 5% more per damage taken(while on fire, max being 50% more)
        */
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);
        meta.setDisplayName(ChatColor.GREEN + "Emerald Dagger");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Has a 10% chance to drop 1-3 Emeralds on a",
                ChatColor.GRAY + "kill. ",
                ChatColor.GRAY + "",
                ChatColor.GRAY + "When paired with Sharpness V, the dagger",
                ChatColor.GRAY + "has a 20% chance to deal 50% more damage."));
        meta.setLocalizedName("emerald_dagger");
        item.setItemMeta(meta);
        return item;
    }
    public ItemStack getTheEmeraldDagger(){
        return getEmeraldDagger();
    }
    private boolean isEmeraldDagger(ItemStack item) {
        if (item == null) {
            return false;
        } else {
            return (item.getItemMeta().getLocalizedName().equals("emerald_dagger"));
        }
    }

    private ItemStack getMagmaCutlass(){
        ItemStack item = new ItemStack(Material.MAGMA_CREAM);
        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);
        meta.setDisplayName(ChatColor.GOLD + "Magma Cutlass");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "When right-clicked, if not on the 15-second",
                ChatColor.GRAY + "cooldown, will send a flaming arrow in the",
                ChatColor.GRAY + "direction you are facing.",
                "",
                ChatColor.GRAY + "When taking fire damage, the cutlass's",
                ChatColor.GRAY + "damage will deal 5% more per damage",
                ChatColor.GRAY + "taken(while on fire, max being 50% more)"));
        meta.setLocalizedName("magma_cutlass");
        item.setItemMeta(meta);
        return item;
    }
    public ItemStack getTheMagmaCutlass(){
        return getMagmaCutlass();
    }
    private boolean isMagmaCutlass(ItemStack item){
        if (item == null){
            return false;
        } else {
            return (item.getItemMeta().getLocalizedName().equals("magma_cutlass"));
        }
    }

    // LISTENERS -- ABILITIES

    @EventHandler
    public void onDamageWithEmeraldDagger(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Player){
            Player p = (Player) e.getDamager();
            if (p.getInventory().getItemInMainHand().getItemMeta().getLocalizedName().equals("emerald_dagger")){
                e.setDamage(10);
            }
        }
    }
    @EventHandler
    public void onEntityDeathEmeraldDagger(EntityDeathEvent e){
        if (e.getEntity().getKiller() instanceof Player){
            Player p = e.getEntity().getKiller();
            if (p.getInventory().getItemInMainHand().equals(Material.EMERALD)
                    && p.getInventory().getItemInMainHand().getItemMeta().getLocalizedName().equals("emerald_dagger")){
                if (rollNumber(0,11) == 1){
                    int emeralds = rollNumber(0,5);
                    p.getInventory().addItem(new ItemStack(Material.EMERALD, emeralds));
                    p.sendMessage(ChatColor.GREEN + "You have received emeralds as a reward for using your Emerald Blade.");
                }
            }
        }
    }

    @EventHandler
    public void onDamageWithMagmaCutlass(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Player){
            Player p = (Player) e.getDamager();
            if (p.getInventory().getItemInMainHand().getItemMeta().getLocalizedName().equals("magma_cutlass")){
                if(Math.floorDiv(p.getFireTicks(), 20) >= 1) {
                    e.setDamage((int) Math.addExact(7, Math.multiplyExact(p.getFireTicks(), (long) 0.05)));
                } else {
                    e.setDamage(7);
                }
            }
        }
    }
    @EventHandler
    public void onClickMagmaCutlass(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.hasItem()) {
                if (isMagmaCutlass(e.getItem())) {
                    Location loc = e.getPlayer().getLocation();
                    Vector dir = loc.getDirection();
                    Arrow arrow = e.getPlayer().getWorld().spawn(loc.add(0, 2, 0), Arrow.class);
                    arrow.setFireTicks(20 * 10);
                    arrow.setVisualFire(true);
                    double speed = 1.5;
                    arrow.setVelocity(dir.multiply(speed));
                }
            }
        }
    }
}
