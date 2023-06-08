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
import com.stigglespatch.main.Dungeon.*;
import com.stigglespatch.main.Dungeon.Cuboids.Cuboid;
import com.stigglespatch.main.Misc.CreativeCommand;
import com.stigglespatch.main.Misc.SMPCommand;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.*;


public final class Main extends JavaPlugin implements Listener {
    public static ArrayList roomsFinished = new ArrayList<Cuboid>();
    public static ArrayList zombieSpawnRooms = new ArrayList<Cuboid>();
    public static boolean closedBossEntry = false;
    public boolean blazingBeastSpawned = false;

    public static int roomNumber = 0;

    public int getRoomNumber(){
        return roomNumber;
    }
    DungeonStartCommand dSC = new DungeonStartCommand();
    //DungeonMobs dungeonMobs = new DungeonMobs();
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

        //40 93 792

        Bukkit.getPluginCommand("dungeon").setExecutor(new DungeonCommand(this));
        Bukkit.getPluginCommand("smp").setExecutor(new SMPCommand());
        Bukkit.getPluginCommand("creative").setExecutor(new CreativeCommand());
        Bukkit.getPluginCommand("start-dungeon").setExecutor(new DungeonStartCommand());
        Bukkit.getPluginCommand("check").setExecutor(new CheckCommand());
        Bukkit.getPluginCommand("get-items").setExecutor(new getItemsCommand());
        Bukkit.getPluginCommand("strayGroup").setExecutor(new doStrayThing(this));
        Bukkit.getPluginCommand("spawn-merchant").setExecutor(new spawnMerchant(this));

        /*
        if (Bukkit.getWorld("smp_cinco") == null) {
            Bukkit.getServer().createWorld(new WorldCreator("smp_cinco"));
        }
        if (Bukkit.getWorld("testdungeon") == null) {
            Bukkit.getServer().createWorld(new WorldCreator("testdungeon"));
        }
        out.println("Generated the third-party worlds.");
         */

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

        //merchant.spawnMerchantRep(new Location(Bukkit.getWorld("world"), 39, 92, 792, 90, 0));

        new BukkitRunnable() {
            public void run() {
                peacesSymphony.checkForPeaceArmor();
            }
        }.runTaskTimer(this, 0, 40);
        new BukkitRunnable() {
            public void run() {
                snowySpawn();
            }
        }.runTaskTimer(this, 200, 20*(60*15));
        new BukkitRunnable() {
            public void run() {
                lunarArmor.checkForLunarArmor();
            }
        }.runTaskTimer(this, 0, 40);
        /*
        new BukkitRunnable() { public void run() {
            spawnStrayGroup(); }
        }.runTaskTimer(this, 20*30, 20*(60*10));
        new BukkitRunnable() { public void run() {
            startForBlazingBeast(); }
        }.runTaskTimer(this, 20*30, 20*(60*30));
        new BukkitRunnable() { public void run() {  merchantTime(); }}.runTaskTimer(this, 20*30, 20*(60*10));
         */

        //CUSTOM CRAFTING RECIPES

        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);
        meta.setDisplayName(ChatColor.GREEN + "Emerald Dagger");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY +  "",
                ChatColor.GOLD +  "-- SPECIAL ITEM --",
                ChatColor.GRAY + "Has a random chance to drop",
                ChatColor.GRAY + "multiple emeralds on a kill. ",
                ChatColor.GRAY + "",
                ChatColor.GRAY + "When enchanted with Sharpness V",
                ChatColor.GRAY + "the dagger unlocks a special",
                ChatColor.GRAY + "ability."));
        meta.setLocalizedName("emerald_dagger");
        item.setItemMeta(meta);

        ItemStack finalItem = new ItemStack(Material.EMERALD);
        ItemMeta finalMeta = finalItem.getItemMeta();
        finalMeta.setUnbreakable(true);
        finalMeta.setDisplayName(ChatColor.GREEN + "Emerald Dagger");
        finalMeta.setLore(Arrays.asList(
                ChatColor.GRAY +  "",
                ChatColor.GOLD +  "-- SPECIAL ITEM --",
                ChatColor.GRAY + "Has a random chance to drop",
                ChatColor.GRAY + "multiple emeralds on a kill. ",
                ChatColor.GRAY + "",
                ChatColor.GRAY + "Since enchanted with Sharpness V,",
                ChatColor.GRAY + "the dagger deals 15-30 damage"));
        finalMeta.setLocalizedName("emerald_dagger");
        finalItem.setItemMeta(finalMeta);

        // Create the enchanted book with Sharpness V
        ItemStack sharpnessBook = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta bookItemMeta = (EnchantmentStorageMeta) sharpnessBook.getItemMeta();
        bookItemMeta.addStoredEnchant(Enchantment.DAMAGE_ALL, 5, true);
        sharpnessBook.setItemMeta(meta);

        // Create the shapeless recipe
        ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(this, "emerald_blade"), finalItem);
        recipe.addIngredient(new RecipeChoice.ExactChoice(sharpnessBook));
        recipe.addIngredient(new RecipeChoice.ExactChoice(item));

        // Register the recipe
        getServer().addRecipe(recipe);



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

    private void snowySpawn() {
        for(Player p : Bukkit.getOnlinePlayers()){
            if (p.getWorld().getBiome(p.getLocation()).equals(Biome.SNOWY_BEACH) ||
                    p.getWorld().getBiome(p.getLocation()).equals(Biome.SNOWY_PLAINS) ||
                    p.getWorld().getBiome(p.getLocation()).equals(Biome.SNOWY_SLOPES) ||
                    p.getWorld().getBiome(p.getLocation()).equals(Biome.SNOWY_TAIGA)){
                Location center = p.getLocation();
                double range = 75;
                double x = center.getX() + (Math.random() * range * 2 - range);
                double y = center.getY();
                double z = center.getZ() + (Math.random() * range * 2 - range);
                Location lowRandomLocation = new Location(center.getWorld(), x, y, z);
                y = lowRandomLocation.getWorld().getHighestBlockYAt(lowRandomLocation) + 2;
                Location highRandomLocation = new Location(center.getWorld(), x, y, z);

                groupLighning(highRandomLocation);
                groupLighning(highRandomLocation);
                groupLighning(highRandomLocation);
                groupLighning(highRandomLocation);
                groupLighning(highRandomLocation);

                new BukkitRunnable() { public void run() { Entities.spawnStrayGroup(highRandomLocation);  } }.runTaskLater(this, 40);

            }
        }
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

        groupLighning(new Location(Bukkit.getWorld("world_nether"), 96, 206, 225));
        groupLighning(new Location(Bukkit.getWorld("world_nether"), 96, 206, 225));
        groupLighning(new Location(Bukkit.getWorld("world_nether"), 96, 206, 225));
        groupLighning(new Location(Bukkit.getWorld("world_nether"), 96, 206, 225));
        groupLighning(new Location(Bukkit.getWorld("world_nether"), 96, 206, 225));
        groupLighning(new Location(Bukkit.getWorld("world_nether"), 96, 206, 225));
        groupLighning(new Location(Bukkit.getWorld("world_nether"), 96, 206, 225));
        groupLighning(new Location(Bukkit.getWorld("world_nether"), 96, 206, 225));
        groupLighning(new Location(Bukkit.getWorld("world_nether"), 96, 206, 225));
        groupLighning(new Location(Bukkit.getWorld("world_nether"), 96, 206, 225));

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

    private void groupLighning(Location location) {
        new BukkitRunnable() { public void run() {
            Bukkit.getWorld("world_nether").strikeLightning(location);
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

    public void merchantTime() {
        World world = Bukkit.getWorld("world"); //World object

        if (!merchant.getMerchantEntities().isEmpty()) {
            for(UUID id : merchant.getMerchantEntities()){ (Bukkit.getEntity(id)).remove(); }
        }

        doMerchantyThing(world);

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
    public void eyeOfEnderUse(EntitySpawnEvent e) {
        if (e.getEntity().equals(EntityType.ENDER_SIGNAL)){
            EnderSignal enderSignal = (EnderSignal) e.getEntity();
            if (e.getEntity().getWorld().equals("world")){
                enderSignal.setTargetLocation(new Location(Bukkit.getWorld("world"), -820, -7, -672));
            }
        }
    }

    @EventHandler
    public void interact(PlayerInteractEvent e){
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            Player p = e.getPlayer();
            if(p.getInventory().getItemInMainHand().equals(Material.ENDER_EYE)){
                Location targetLocation = new Location(Bukkit.getWorld("world"), -820, -7, -672);

                EnderSignal enderSignal = Bukkit.getWorld("world").spawn(p.getLocation().add(0,2,0), EnderSignal.class);
                enderSignal.setDropItem(false);
                enderSignal.setTargetLocation(targetLocation);
                int eyeAmount = p.getInventory().getItemInMainHand().getAmount();
                enderSignal.setDespawnTimer(0);
                int newEyeAmount = eyeAmount-1;
                p.getInventory().getItemInMainHand().setAmount(newEyeAmount);
            }
        }
    }
    /*@EventHandler
    public void onNether(PlayerChangedWorldEvent e) {
        if (e.getFrom().getName().equals("world")) {
            if (!e.getPlayer().getWorld().equals("world_end")) {
                e.getPlayer().teleport(new Location(Bukkit.getWorld("world_nether"), 68, 194, 61, -180, 0));
            }
        } else if (e.getFrom().getName().equals("world_nether")){
            e.getPlayer().teleport(e.getPlayer().getBedSpawnLocation());
        }
    }*/
    public NamespacedKey getNamespacedKey() { return pendant; }

    public static int rollNumber(int min, int max){
        Random rand = new Random();
        int randomNumber = rand.nextInt(max - min + 1) + min;

        return randomNumber;
    }

    private void doMerchantyThing(World world) {
        inventoryManager.getMerchantUUIDMap().clear(); //Clear the map
        merchant.getMerchantEntities().clear();

        //Community Center Villager
        Villager vilTheVillager = merchant.spawnLostMerchant(new Location(world, 54, 101, 785)); //Make villager


        int spawnSpot = rollNumber(1, 9);
        if (spawnSpot == 1) {
            //Snowy Villager
            merchant.spawnLostMerchant(new Location(world, 1332, 12, 3 - 824));
            merchant.spawnLostMerchant(new Location(world, -904, 72, 1517));
            merchant.spawnLostMerchant(new Location(world, 1123, 67, -203));
        } else if (spawnSpot == 2) {
            //Plains Villager
            merchant.spawnLostMerchant(new Location(world, -302, 69, 315));
            merchant.spawnLostMerchant(new Location(world, -904, 72, 1517));
            merchant.spawnLostMerchant(new Location(world, 1332, 12, 3 - 824));
        } else if (spawnSpot == 3) {
            //Jungle Villager
            merchant.spawnLostMerchant(new Location(world, -904, 72, 1517));
            merchant.spawnLostMerchant(new Location(world, 1332, 12, 3 - 824));
            merchant.spawnLostMerchant(new Location(world, 1123, 67, -203));
        } else if (spawnSpot == 4) {
            //Desert Villager
            merchant.spawnLostMerchant(new Location(world, -903, 72, 701));
            merchant.spawnLostMerchant(new Location(world, -904, 72, 1517));
            merchant.spawnLostMerchant(new Location(world, -904, 72, 1517));
        } else if (spawnSpot == 5) {
            //Mesa Villager
            merchant.spawnLostMerchant(new Location(world, -904, 72, 1517));
            merchant.spawnLostMerchant(new Location(world, -456, 117, -1195));
            merchant.spawnLostMerchant(new Location(world, 1123, 67, -203));
        } else if (spawnSpot == 6) {
            //Savanna Villager
            merchant.spawnLostMerchant(new Location(world, -873, 66, -312));
            merchant.spawnLostMerchant(new Location(world, 1332, 12, 3 - 824));
            merchant.spawnLostMerchant(new Location(world, 816, 110, 510));
        } else if (spawnSpot == 7) {
            //Forest Villager
            merchant.spawnLostMerchant(new Location(world, 816, 110, 510));
            merchant.spawnLostMerchant(new Location(world, 1123, 67, -203));
            merchant.spawnLostMerchant(new Location(world, -456, 117, -1195));
        } else if (spawnSpot == 8) {
            //Taiga Villager
            merchant.spawnLostMerchant(new Location(world, 1123, 67, -203));
            merchant.spawnLostMerchant(new Location(world, -873, 66, -312));
            merchant.spawnLostMerchant(new Location(world, -904, 72, 1517));
        } else {
            merchant.spawnLostMerchant(new Location(world, 1332, 123, -824));
            merchant.spawnLostMerchant(new Location(world, -302, 69, 315));
            merchant.spawnLostMerchant(new Location(world, -904, 72, 1517));
            merchant.spawnLostMerchant(new Location(world, -903, 72, 701));
            merchant.spawnLostMerchant(new Location(world, -456, 117, -1195));
            merchant.spawnLostMerchant(new Location(world, -873, 66, -312));
            merchant.spawnLostMerchant(new Location(world, 816, 110, 510));
            merchant.spawnLostMerchant(new Location(world, 1123, 67, -203));
            Bukkit.broadcastMessage(ChatColor.GOLD + "The Merchant Coalition has sent ALL of its merchants into the world! They are in every available location! Take this time to go and stop by!");
        }
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
