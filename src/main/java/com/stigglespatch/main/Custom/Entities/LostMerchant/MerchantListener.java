package com.stigglespatch.main.Custom.Entities.LostMerchant;

import com.stigglespatch.main.Custom.InventoryManager;
import com.stigglespatch.main.Dungeon.Cuboids.Cuboid;
import com.stigglespatch.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class MerchantListener implements Listener {
    private Main plugin; // Add a reference to the plugin instance
    Map<UUID, Boolean> merchantCheckList = new HashMap<UUID, Boolean>();


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
                if (e.getRightClicked().getCustomName().equals(ChatColor.AQUA + "Merchant Marketeer")) {
                    Villager m = (Villager) e.getRightClicked();
                    if (merchantCheckList.get(m.getUniqueId()) == null) {
                        inv = inventoryManager.makeInventory(p, 54, ChatColor.GRAY + "Lost Merchant Shop", inv);
                        inventoryManager.setInvMap(m.getUniqueId(), inv);
                        merchantCheckList.put(m.getUniqueId(), true);
                        p.openInventory(inv);

                    } else {
                        p.openInventory(inventoryManager.getInventoryFromMap(m.getUniqueId()));
                    }

                } else if (e.getRightClicked().getCustomName().equals(ChatColor.AQUA + "Merchant Representative")) {
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
                    }.runTaskLater(plugin, 100);
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

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(ChatColor.GRAY + "Lost Merchant Shop") && e.getCurrentItem() != null){
            e.setCancelled(true);

        }

    }

    @EventHandler
    public void blockBroken(BlockBreakEvent e){
        Cuboid plainsShop = new Cuboid(
                new Location(Bukkit.getWorld("world"),-298, 73, 313),
                new Location(Bukkit.getWorld("world"),-306, 68, 317));
        Cuboid jungleShop = new Cuboid(
                new Location(Bukkit.getWorld("world"),-907, 71, 1514),
                new Location(Bukkit.getWorld("world"),-902, 75, 1520));
        Cuboid desertShop = new Cuboid(
                new Location(Bukkit.getWorld("world"),-901, 71, 698),
                new Location(Bukkit.getWorld("world"),-906, 76, 703));
        Cuboid mesaShop = new Cuboid(
                new Location(Bukkit.getWorld("world"),-452, 116, -1199),
                new Location(Bukkit.getWorld("world"),-462, 121, -1189));
        Cuboid savShop = new Cuboid(
                new Location(Bukkit.getWorld("world"),-879, 69, -317),
                new Location(Bukkit.getWorld("world"),-870, 65, -309));
        Cuboid forestsShop = new Cuboid(
                new Location(Bukkit.getWorld("world"),813, 109, 512),
                new Location(Bukkit.getWorld("world"),818, 114, 507));
        Cuboid threeBiomeShop = new Cuboid(
                new Location(Bukkit.getWorld("world"),1125, 66, -199),
                new Location(Bukkit.getWorld("world"),1117, 70, -207));
        Cuboid snowShop = new Cuboid(
                new Location(Bukkit.getWorld("world"),1335, 128, -825),
                new Location(Bukkit.getWorld("world"),1329, 93, -819));

    }

}
