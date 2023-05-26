package com.stigglespatch.main;

import com.stigglespatch.main.Custom.Entities.Entities;
import com.stigglespatch.main.Custom.Entities.LostMerchant.LostMerchant;
import com.stigglespatch.main.Custom.Entities.LostMerchant.MerchantListener;
import com.stigglespatch.main.Custom.InventoryManager;
import com.stigglespatch.main.Custom.Items.Armor.AnarchysWardrobe;
import com.stigglespatch.main.Custom.Items.Armor.LunarArmor;
import com.stigglespatch.main.Custom.Items.Armor.PeacesSymphony;
import com.stigglespatch.main.Custom.Items.Bows.BoomBow;
import com.stigglespatch.main.Custom.Items.Bows.GlowBow;
import com.stigglespatch.main.Custom.Items.Pickaxes;
import com.stigglespatch.main.Custom.Items.Swords;
import com.stigglespatch.main.Database.ConnectionListener;
import com.stigglespatch.main.Database.Database;
import com.stigglespatch.main.Database.PlayerManager;
import com.stigglespatch.main.Dungeon.*;
import com.stigglespatch.main.Dungeon.Cuboids.Cuboid;
import com.stigglespatch.main.Misc.CreativeCommand;
import com.stigglespatch.main.Misc.SMPCommand;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.lang.System.*;


public final class Main extends JavaPlugin implements Listener {
    public static ArrayList roomsFinished = new ArrayList<Cuboid>();
    public static ArrayList zombieSpawnRooms = new ArrayList<Cuboid>();
    public static boolean closedBossEntry = false;
    public boolean blazingBeastSpawned = false;


    private Database database;
    public static int roomNumber = 0;

    public int getRoomNumber(){
        return roomNumber;
    }
    DungeonStartCommand dSC = new DungeonStartCommand();
    //DungeonMobs dungeonMobs = new DungeonMobs();
    private PlayerManager playerManager;
    PeacesSymphony peacesSymphony = new PeacesSymphony();
    LunarArmor lunarArmor = new LunarArmor();
    LostMerchant merchant = new LostMerchant();
    InventoryManager inventoryManager = new InventoryManager();
    MerchantListener merchantListener = new MerchantListener(this);
    public final NamespacedKey pendant = new NamespacedKey(this, "pendant");
    Inventory placeholderInventory;

    DungeonManager dm = new DungeonManager(this);

    @Override
    public void onEnable() {

        database = new Database();
        try {
            database.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        playerManager = new PlayerManager();

        //40 93 792

        Bukkit.getPluginCommand("dungeon").setExecutor(new DungeonCommand(this));
        Bukkit.getPluginCommand("smp").setExecutor(new SMPCommand());
        Bukkit.getPluginCommand("creative").setExecutor(new CreativeCommand());
        Bukkit.getPluginCommand("start-dungeon").setExecutor(new DungeonStartCommand());
        Bukkit.getPluginCommand("check").setExecutor(new CheckCommand());
        Bukkit.getPluginCommand("get-items").setExecutor(new getItemsCommand());
        Bukkit.getPluginCommand("strayGroup").setExecutor(new doStrayThing(this));
        Bukkit.getPluginCommand("spawn-merchant").setExecutor(new spawnMerchant());

        if (Bukkit.getWorld("smp_cinco") == null) {
            Bukkit.getServer().createWorld(new WorldCreator("smp_cinco"));
        }
        if (Bukkit.getWorld("testdungeon") == null) {
            Bukkit.getServer().createWorld(new WorldCreator("testdungeon"));
        }
        out.println("Generated the third-party worlds.");

        Bukkit.getPluginManager().registerEvents(new ConnectionListener(this), this);
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new DungeonStartCommand(), this);
        Bukkit.getPluginManager().registerEvents(new DungeonMobs(), this);
        Bukkit.getPluginManager().registerEvents(new Swords(), this);
        Bukkit.getPluginManager().registerEvents(new BoomBow(), this);
        Bukkit.getPluginManager().registerEvents(new GlowBow(), this);
        Bukkit.getPluginManager().registerEvents(new Pickaxes(), this);
        Bukkit.getPluginManager().registerEvents(new AnarchysWardrobe(), this);
        Bukkit.getPluginManager().registerEvents(new PeacesSymphony(), this);
        Bukkit.getPluginManager().registerEvents(new Entities(), this);
        Bukkit.getPluginManager().registerEvents(new MerchantListener(this), this);

        merchant.spawnMerchantRep(new Location(Bukkit.getWorld("world"), 40, 93, 792));

        new BukkitRunnable() {
            public void run() {
                peacesSymphony.checkForPeaceArmor();
            }
        }.runTaskTimer(this, 0, 40);
        new BukkitRunnable() {
            public void run() {
                lunarArmor.checkForLunarArmor();
            }
        }.runTaskTimer(this, 0, 40);
        new BukkitRunnable() { public void run() {
            spawnStrayGroup(); }
        }.runTaskTimer(this, 20*30, 20*(60*10));
        new BukkitRunnable() { public void run() {
            startForBlazingBeast(); }
        }.runTaskTimer(this, 20*30, 20*(60*30));
        new BukkitRunnable() { public void run() {
            merchantTime(); }
        }.runTaskTimer(this, 20*30, 20*(60*30));

        //CUSTOM CRAFTING RECIPES

        ItemStack customItem = new ItemStack(Material.EMERALD);
        ItemMeta meta = customItem.getItemMeta();
        meta.setLocalizedName("emerald_blade");
        customItem.setItemMeta(meta);



        /*
        ItemStack emerladBlade = new ItemStack(Material.EMERALD);
        ItemMeta emeraldItemMeta = customItem.getItemMeta();
        emerladBlade.getItemMeta();
        emeraldItemMeta.addEnchant(Enchantment.DAMAGE_ALL, 5 ,true);
        emeraldItemMeta.setUnbreakable(true);
        emeraldItemMeta.setDisplayName(ChatColor.GREEN + "Emerald Dagger");
        emeraldItemMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "",
                ChatColor.GOLD + "-- SPECIAL ITEM --",
                ChatColor.GRAY + "Has a random chance to drop",
                ChatColor.GRAY + "multiple emeralds on a kill. ",
                ChatColor.GRAY + "",
                ChatColor.GRAY + "Since enchanted with Sharpness V,",
                ChatColor.GRAY + "the dagger has a chance",
                ChatColor.GRAY + "to deal 15-35 damage."));
        emeraldItemMeta.setLocalizedName("emerald_dagger");
        emerladBlade.setItemMeta(emeraldItemMeta);

        ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(this, "enchant"), emerladBlade);
        recipe.addIngredient(Material.ENCHANTED_BOOK);
        recipe.addIngredient(new RecipeChoice.ExactChoice(customItem));
        Bukkit.addRecipe(recipe);
        */
    }

    private void getEntitiesInRoom(Cuboid collectionRoom) {
        for(Block block : collectionRoom.getBlocks()){
            if(block.getType().equals(Material.LIGHT_GRAY_CANDLE)){
                DungeonMobs.spawnDungeonZombie(block.getLocation().add(0,1,0));
            } else if (block.getType().equals(Material.GRAY_CANDLE)){
                DungeonMobs.spawnDungeonSkeleton(block.getLocation().add(0,1,0));
            } else if (block.getType().equals(Material.CYAN_CANDLE)){
                DungeonMobs.spawnDungeonCreeper(block.getLocation().add(0,1,0));
            }
        }
    }

    public void spawnStrayGroup(){
        for(Player player : Bukkit.getOnlinePlayers()){
            for(Entity e : player.getNearbyEntities(128, 128, 128)){
                if (e instanceof Stray){
                    if (Entities.rollNumber(1,5) == 5){
                        Bukkit.getWorld("world").strikeLightning(e.getLocation());
                        Entities.spawnStrayGroup(e.getLocation());
                        e.remove();
                        player.sendMessage(ChatColor.RED + "[Warning] A stray group has spawned near you, stay on the lookout!");
                    }
                }
            }
        }
    }

    public void startForBlazingBeast(){
        Cuboid beastTower = new Cuboid(
                new Location(Bukkit.getWorld("world_nether"), 99, 206, 228),
                new Location(Bukkit.getWorld("world_nether"), 93, 206, 222));
        if (!blazingBeastSpawned) {
            for(Block block : beastTower.getBlocks()){
                block.setType(Material.BARRIER);
            }
            new BukkitRunnable() { public void run() {
                for(Block block : beastTower.getBlocks()){
                    block.setType(Material.AIR);
                }
                }
            }.runTaskLater(this, 20*(60*25));
            spawnTheBeast();
        }
    }

    public void spawnTheBeast(){

        groupLighning();
        groupLighning();
        groupLighning();
        groupLighning();
        groupLighning();
        groupLighning();
        groupLighning();
        groupLighning();
        groupLighning();
        groupLighning();

        for (Player p : Bukkit.getOnlinePlayers()){
            if (p.getWorld().getName().equals("world_nether")) {
                p.sendMessage(ChatColor.RED+"The Blazing Beast has spawned! Take this opportunity to acquire a rare and unique custom item!");
                p.playSound(p, Sound.ENTITY_ENDER_DRAGON_AMBIENT, 15, .1F);
                p.playSound(p, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 15, .01F);
                p.playSound(p, Sound.ENTITY_ENDER_DRAGON_GROWL, 15, .1F);
            }
        }

        Entities.spawnBlazingBeast(new Location(Bukkit.getWorld("world_nether"),96 ,206 ,225));
        blazingBeastSpawned = true;
    }

    private void groupLighning() {
        new BukkitRunnable() { public void run() {
            Bukkit.getWorld("world_nether").strikeLightningEffect(new Location(Bukkit.getWorld("world_nether"),96 ,206 ,225));
        }
        }.runTaskLater(this, 15);
    }

    @EventHandler
    public void onKill(EntityDeathEvent e){
        if (e.getEntity().getCustomName() != null && e.getEntity().getCustomName().equals(ChatColor.GOLD + "Blazing Beast") && e.getEntity().getKiller() != null){
            blazingBeastSpawned = false;
            if (e.getEntity().getKiller() != null){
                Player p = e.getEntity().getKiller();
                p.sendMessage(ChatColor.GREEN + "The " + ChatColor.GOLD + "Magma Cutlass " + ChatColor.GREEN + "has been dropped as a reward for killing the Blazing Beast!");
                p.playSound(p.getLocation(), Sound.ENTITY_ALLAY_AMBIENT_WITH_ITEM, 10, 1);
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, .5F);

                ItemStack cutlass = new ItemStack(Material.MAGMA_CREAM);
                ItemMeta meta = cutlass.getItemMeta();
                meta.setUnbreakable(true);
                meta.setDisplayName(ChatColor.GOLD + "Magma Cutlass");
                meta.setLore(Arrays.asList(
                        ChatColor.GRAY +  "",
                        ChatColor.GOLD +  "-- SPECIAL ITEM --",
                        ChatColor.GRAY + "When right-clicked, it uses 1",
                        ChatColor.GRAY + "experience level to shoot a",
                        ChatColor.GRAY + "flaming arrow in the direction",
                        ChatColor.GRAY + "you are facing.",
                        "",
                        ChatColor.GRAY + "While taking damage by fire,",
                        ChatColor.GRAY + "you will deal significantly more",
                        ChatColor.GRAY + "damage to all entities."));
                meta.setLocalizedName("magma_cutlass");
                cutlass.setItemMeta(meta);

                Bukkit.getWorld("world_nether").dropItemNaturally(e.getEntity().getLocation(), cutlass);

                Cuboid beastTower = new Cuboid(
                        new Location(Bukkit.getWorld("world_nether"), 99, 206, 228),
                        new Location(Bukkit.getWorld("world_nether"), 93, 206, 222));
                for (Block block : beastTower.getBlocks()){
                    block.setType(Material.SOUL_SAND);
                }

                killAfterSpawns(e.getEntity().getKiller());
            }


        }
    }

    private void killAfterSpawns(Entity e) {
        new BukkitRunnable() {
            public void run() {
                for (Entity magma : e.getNearbyEntities(100, 100, 100)) {
                    if (magma instanceof MagmaCube) {
                        magma.remove();
                    }
                }
            }
        }.runTaskLater(this, 5);
        new BukkitRunnable() {
            public void run() {
                for (Entity magma : e.getNearbyEntities(100, 100, 100)) {
                    if (magma instanceof MagmaCube) {
                        magma.remove();
                    }
                }
            }
        }.runTaskLater(this, 5);
        new BukkitRunnable() {
            public void run() {
                for (Entity magma : e.getNearbyEntities(100, 100, 100)) {
                    if (magma instanceof MagmaCube) {
                        magma.remove();
                    }
                }
            }
        }.runTaskLater(this, 5);
        new BukkitRunnable() {
            public void run() {
                for (Entity magma : e.getNearbyEntities(100, 100, 100)) {
                    if (magma instanceof MagmaCube) {
                        magma.remove();
                    }
                }
            }
        }.runTaskLater(this, 5);
        new BukkitRunnable() {
            public void run() {
                for (Entity magma : e.getNearbyEntities(100, 100, 100)) {
                    if (magma instanceof MagmaCube) {
                        magma.remove();
                    }
                }
            }
        }.runTaskLater(this, 5);
        new BukkitRunnable() {
            public void run() {
                for (Entity magma : e.getNearbyEntities(100, 100, 100)) {
                    if (magma instanceof MagmaCube) {
                        magma.remove();
                    }
                }
            }
        }.runTaskLater(this, 5);
    }

    private void merchantTime() {
        int spawnSpot = rollNumber(1,10);
        if (spawnSpot == 1) {
            //54, 101, 785
            Villager vilTheVillager = merchant.spawnLostMerchant(new Location(Bukkit.getWorld("world"),54, 101, 785)); //Make villager
            inventoryManager.setInvMap(vilTheVillager.getUniqueId(), inventoryManager.getInventoryFromMap(vilTheVillager.getUniqueId())); //Assign inventory

        } else if (spawnSpot == 2){
            //
        } else if (spawnSpot == 3){

        } else if (spawnSpot == 4){

        } else if (spawnSpot == 5){

        } else if (spawnSpot == 6){

        } else if (spawnSpot == 7){

        } else if (spawnSpot == 8){

        } else if (spawnSpot == 9){

        } else if (spawnSpot == 10){

        } else {

        }
    }

    @Override
    public void onDisable(){
        database.disconnect();
        //dSC.getPlayersList().clear();
        //dSC.getAlivePlayers().clear();
        //roomsFinished.clear();
    }

    @EventHandler
    public void onBoomBoom(BlockExplodeEvent e){
        if(e.getBlock().getWorld().equals("testdungeon")){
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        /*Player p = e.getPlayer();
        if (dSC.getPlayersList().contains(p)){
            dSC.getPlayersList().remove(p);
        }
        if (dSC.getAlivePlayers().contains(p)){
            dSC.getAlivePlayers().remove(p);
        }*/
    }

    @EventHandler
    public void onPortal(PortalCreateEvent e) {
        if(!e.getEntity().isOp()){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onNether(PlayerChangedWorldEvent e) {
        if (e.getFrom().getName().equals("world")) {
            e.getPlayer().teleport(new Location(Bukkit.getWorld("world_nether"), 68, 194, 61, -180, 0));
        } else if (e.getFrom().getName().equals("world_nether")){
            e.getPlayer().teleport(new Location(Bukkit.getWorld("world"),6, 92, 779, -90, 0));
        }
    }

    public Database getDatabase() { return database; }
    public PlayerManager getPlayerManager() { return playerManager; }
    public NamespacedKey getNamespacedKey() { return pendant; }

    public static int rollNumber(int min, int max){
        Random rand = new Random();
        int randomNumber = rand.nextInt(max) + min;

        return randomNumber;
    }



}

/* INSERT INTO DATABASE
    PreparedStatement ps = database.getConnection().prepareStatement("INSERT INTO table (C1,C2,C4) VALUES (?,?,?);"); //insert
            ps.setString(1,"banana");
                    ps.setInt(2,2);
                    ps.setBoolean(3,true);
                    ps.executeUpdate();


                    PreparedStatement ps2 = database.getConnection().prepareStatement("UPDATE table SET column = ? WHERE column2 = ?;"); //update
                    ps2.setString(1,"banana");
                    ps2.setInt(2,2);
                    ps.executeUpdate();
INSERT INTO DATABASE

DELETE FROM DATABASE
                    PreparedStatement ps3 = database.getConnection().prepareStatement("DELETE FROM table WHERE column =?"); //delete
                    ps3.setString(1,"banana");
                    ps.executeUpdate();
DELETE FROM DATABASE

RETRIEVE DATA FROM DATABASE
                    //Example: replace UUID with * or UUID, RANK (to get all UUID/RANK whom it applies to | or everything that relates to people whom it is true for.)
                    PreparedStatement ps4 = database.getConnection().prepareStatement("SELECT UUID FROM table WHERE column = ?"); //retrieve data from all people which the condition applies to
                    ResultSet rs = ps4.executeQuery();
                    while (rs.next()) {
                    System.out.println(rs.getString("UUID"));
                    }
RETRIEVE DATA FROM DATABASE
 */
        /*
        Cuboid entryCuboid = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),12, -35, 195),
                new Location(Bukkit.getWorld("testdungeon"),25, -45, 184));
        Cuboid fillerRoomMine = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),11, -36, 182),
                new Location(Bukkit.getWorld("testdungeon"),-5, -45, 199));
        zombieSpawnRooms.add(fillerRoomMine);
        Cuboid targetRoom = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),24,-31,181),
                new Location(Bukkit.getWorld("testdungeon"),-4,-53,149));
        zombieSpawnRooms.add(targetRoom);
        Cuboid fillerRoomDrop = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),-3,-39,125),
                new Location(Bukkit.getWorld("testdungeon"),11,-59,149));
        zombieSpawnRooms.add(fillerRoomDrop);
        Cuboid collectionRoom = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),11,-48,124),
                new Location(Bukkit.getWorld("testdungeon"),-16,-58,111));
        zombieSpawnRooms.add(collectionRoom);
        Cuboid mobRoomNormal = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),12,-49,110),
                new Location(Bukkit.getWorld("testdungeon"),28,-58,127));
        zombieSpawnRooms.add(mobRoomNormal);
        Cuboid mobRoomLava = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),27,-49,93),
                new Location(Bukkit.getWorld("testdungeon"),10,-58,110));
        zombieSpawnRooms.add(mobRoomLava);
        Cuboid bossRoom = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),62,-23,115),
                new Location(Bukkit.getWorld("testdungeon"),28,-59,88));
        Cuboid finalRoom = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),63,-54,107),
                new Location(Bukkit.getWorld("testdungeon"),109,-27,96));
        Cuboid finalRoomTriggerWall = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),32,-50,94),
                new Location(Bukkit.getWorld("testdungeon"),35,-57,108));
        Cuboid finalRoomWall = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),28,-57,99),
                new Location(Bukkit.getWorld("testdungeon"),28,-54,103));
        Cuboid finalRoomTP = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),109,-49,97),
                new Location(Bukkit.getWorld("testdungeon"),90,-41,105));
        Cuboid tutorialDungeon = new Cuboid(
                new Location(Bukkit.getWorld("testdungeon"),111, -64, 81),
                new Location(Bukkit.getWorld("testdungeon"),-20, -14, 211));


        Bukkit.getScheduler().runTaskTimer(this, () -> {
                for (Player p : dSC.getPlayersList()) {
                    if (entryCuboid.contains(p.getLocation())){
                        if (!roomsFinished.contains(entryCuboid)) {
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("⬛⬜⬜⬜⬜⬜⬜⬜⬜"));
                            p.sendMessage(ChatColor.RED +"[WARNING] By moving on the the next room, no one else will be able to join!");
                        } else {
                            p.teleport(p.getLocation().subtract(2,0,0));
                            p.sendMessage(ChatColor.RED + "You have already passed this room, you may not go back!");
                        }

                    } else if(fillerRoomMine.contains(p.getLocation())){
                        if (!roomsFinished.contains(fillerRoomMine)) {
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("⬛⬛⬜⬜⬜⬜⬜⬜⬜"));
                            if (dSC.getPlayersList().size() > 4){
                                dSC.getPlayersList().add(p);
                            }

                            roomsFinished.add(entryCuboid);
                        } else {
                            p.teleport(p.getLocation().subtract(0,0,2));
                            p.sendMessage(ChatColor.RED + "You have already passed this room, you may not go back!");
                        }

                    } else if(targetRoom.contains(p.getLocation())){
                        if (!roomsFinished.contains(targetRoom)) {
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("⬛⬛⬛⬜⬜⬜⬜⬜⬜"));
                            roomsFinished.add(fillerRoomMine);
                        } else {
                            p.teleport(p.getLocation().subtract(0,0,2));
                            p.sendMessage(ChatColor.RED + "You have already passed this room, you may not go back!");
                        }

                    } else if(fillerRoomDrop.contains(p.getLocation())){
                        if (!roomsFinished.contains(fillerRoomDrop)) {
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("⬛⬛⬛⬛⬜⬜⬜⬜⬜"));
                            roomsFinished.add(targetRoom);
                        } else {
                            p.teleport(p.getLocation().subtract(0,0,2));
                            p.sendMessage(ChatColor.RED + "You have already passed this room, you may not go back!");
                        }

                    } else if(collectionRoom.contains(p.getLocation())){
                        if (!roomsFinished.contains(collectionRoom)) {
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("⬛⬛⬛⬛⬛⬜⬜⬜⬜"));
                            roomsFinished.add(fillerRoomDrop);
                        } else {
                            p.teleport(p.getLocation().add(2,0,0));
                            p.sendMessage(ChatColor.RED + "You have already passed this room, you may not go back!");
                        }

                    } else if(mobRoomNormal.contains(p.getLocation())){
                        if (!roomsFinished.contains(mobRoomNormal)) {
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("⬛⬛⬛⬛⬛⬛⬜⬜⬜"));
                            roomsFinished.add(collectionRoom);
                        } else {
                            p.teleport(p.getLocation().subtract(0,0,2));
                            p.sendMessage(ChatColor.RED + "You have already passed this room, you may not go back!");
                        }

                    } else if(mobRoomLava.contains(p.getLocation())){
                        if (!roomsFinished.contains(mobRoomLava)) {
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("⬛⬛⬛⬛⬛⬛⬛⬜⬜"));
                            roomsFinished.add(mobRoomNormal);
                        } else {
                            p.teleport(p.getLocation().add(5,0,0));
                            p.sendMessage(ChatColor.RED + "You have already passed this room, you may not go back!");
                        }

                    } else if(bossRoom.contains(p.getLocation())){
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("⬛⬛⬛⬛⬛⬛⬛⬛⬜"));

                        if (finalRoomTriggerWall.contains(p.getLocation()) && closedBossEntry == false){
                            for (Block block : finalRoomWall.getBlocks()){
                                block.setType(Material.DEEPSLATE_TILE_WALL);
                            }
                            p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 1, (float) .5);
                            p.sendMessage(ChatColor.RED + "The door has locked! you must fight the boss to find your way out of this dungeon, best of luck!");
                            closedBossEntry = true;
                        }

                    } else if(finalRoom.contains(p.getLocation())){
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("DUNGEON COMPLETED"));
                        if (finalRoomTP.contains(p.getLocation())){
                            for (Block block : finalRoomWall.getBlocks()){
                                block.setType(Material.AIR);
                            }
                            p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 100, 1);
                            p.playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 100, 1);
                            p.teleport(Bukkit.getWorld("testdungeon").getBlockAt(158,-20,18).getLocation());
                            p.sendMessage(ChatColor.GREEN + "You have defeated the boss! Congratulations!");
                            closedBossEntry = false;
                            Bukkit.broadcastMessage(ChatColor.YELLOW +"The dungeon has been reset! Do /start-dungeon to begin!");
                            roomsFinished.clear();
                            for (Player player : dSC.getPlayersList()) {
                                player.setGameMode(GameMode.ADVENTURE);
                                player.setInvisible(false);
                                player.setAllowFlight(false);
                                player.setFlying(false);
                            }
                            for (Entity e : tutorialDungeon.getWorld().getEntities()){
                                if (tutorialDungeon.contains(e.getLocation())){
                                    e.remove();
                                }
                            }
                            dSC.getPlayersList().clear();
                            dSC.getAlivePlayers().clear();

                        }
                    } else {
                        return;
                    }
                }
        },0, 5);

        int min = 60;
        int max = 100;
        int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
        long timeBeforeNextSpawn = random_int;


        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player p : dSC.getAlivePlayers()){
                if(fillerRoomMine.contains(p.getLocation())){
                    getEntitiesInRoom(fillerRoomMine);

                } else if(targetRoom.contains(p.getLocation())){
                    getEntitiesInRoom(targetRoom);

                } else if(fillerRoomDrop.contains(p.getLocation())){
                    getEntitiesInRoom(fillerRoomDrop);

                } else if(collectionRoom.contains(p.getLocation())){
                    getEntitiesInRoom(collectionRoom);

                } else if(mobRoomNormal.contains(p.getLocation())){
                    getEntitiesInRoom(mobRoomNormal);

                } else if(mobRoomLava.contains(p.getLocation())){
                    getEntitiesInRoom(mobRoomLava);
                }
            }
        },0, timeBeforeNextSpawn);

                public Location getRandomLocation(Location center, double range) {
            double x = center.getX() + (Math.random() * range * 2 - range);
            double y = center.getY();
            double z = center.getZ() + (Math.random() * range * 2 - range);
            Location randomLocation = new Location(center.getWorld(), x, y, z);
            return randomLocation;
        }

        Location dropLocation = getRandomLocation(loc, borderSize);

        */
