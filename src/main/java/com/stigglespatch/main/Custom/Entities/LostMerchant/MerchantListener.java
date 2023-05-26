package com.stigglespatch.main.Custom.Entities.LostMerchant;

import com.stigglespatch.main.Custom.InventoryManager;
import com.stigglespatch.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;


public class MerchantListener implements Listener {
    private Main plugin; // Add a reference to the plugin instance

    public MerchantListener(Main plugin) {
        this.plugin = plugin;
    }
    InventoryManager inventoryManager = new InventoryManager();
    private Inventory inv;

    public Inventory getInventory(UUID id){  return inventoryManager.getInventoryFromMap(id);  }
    @EventHandler
    public void onInteraction(PlayerInteractAtEntityEvent e){
        if(e.getRightClicked().getType().equals(EntityType.VILLAGER)) {
            if (e.getRightClicked().isCustomNameVisible()) {
                Player p = e.getPlayer();
                System.out.println("Checking what was clicked");
                if (e.getRightClicked().getCustomName().equals(ChatColor.AQUA + "Merchant Representative")) {
                    System.out.println("Lost man");
                    Villager m = (Villager) e.getRightClicked();


                    p.openInventory(getInventory(m.getUniqueId())); //TODO FIX ERROR

                }else if (e.getRightClicked().getCustomName().equals(ChatColor.AQUA + "A Merchant Representative")) {
                    Villager m = (Villager) e.getRightClicked();
                    p.playSound(p, Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1);
                    p.sendMessage(ChatColor.WHITE + "<Merchant Representative> Greetings! I represent the Merchant Coalition, dedicated to helping players and acquiring resources. " +
                            "During your exploration, you'll encounter our many merchants. " +
                            "Don't forget to pay them a visit! Our trades refresh every 10 minutes, offering new opportunities. \n");
                    new BukkitRunnable() {
                        public void run() {
                            p.playSound(p, Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1);
                            p.sendMessage(ChatColor.WHITE + "\n<Merchant Representative> Exciting surprises await you! " +
                                    "Our trades occasionally feature remarkable and exclusive items. Stay alert for our presence and make the most of our offerings.");
                        }
                    }.runTaskLater(plugin, 20*5);
                    new BukkitRunnable() {
                        public void run() {
                            p.playSound(p, Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1);
                            p.sendMessage(ChatColor.WHITE + "\n<Merchant Representative> Here's a list of some of the items we offer: " +
                                    "\n [] Grappling Hook" +
                                    "\n [] Smurfs Handy Tool" +
                                    "\n [] Emerald Blade" +
                                    "\n [] Moon Shards" +
                                    "\n [] Bagels");
                        }
                    }.runTaskLater(plugin, 100);

                    new BukkitRunnable() {
                        public void run() {
                            p.playSound(p, Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1);
                            p.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "Merchant Representative whispers to you: My workers also change their location every 10 minutes as well! They may not always be in the same spot!");
                        }
                    }.runTaskLater(plugin, 160);
                }
            }
        }
    }

}
