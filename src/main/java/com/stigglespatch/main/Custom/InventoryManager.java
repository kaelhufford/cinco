package com.stigglespatch.main.Custom;

import com.stigglespatch.main.Custom.Items.Armor.AnarchysWardrobe;
import com.stigglespatch.main.Custom.Items.Armor.LunarArmor;
import com.stigglespatch.main.Custom.Items.Armor.PeacesSymphony;
import com.stigglespatch.main.Custom.Items.BAGEL;
import com.stigglespatch.main.Custom.Items.GrapplingHook;
import com.stigglespatch.main.Custom.Items.Pickaxes;
import com.stigglespatch.main.Custom.Items.Swords;
import com.stigglespatch.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.logging.Level;

public class InventoryManager {
    Map<UUID, Inventory> merchantList = new HashMap<UUID, Inventory>();

    AnarchysWardrobe anarchysWardrobe = new AnarchysWardrobe();
    PeacesSymphony peacesSymphony = new PeacesSymphony();
    LunarArmor lunarArmor = new LunarArmor();
    GrapplingHook grapplingHook = new GrapplingHook();
    BAGEL bagel = new BAGEL();
    Pickaxes pickaxes = new Pickaxes();
    Swords swords = new Swords();

    public Inventory makeInventory(Player player, int size, String title, Inventory inventory){
        inventory = Bukkit.createInventory(player, size, title);

        return inventory;
    }

    public Inventory addItem(Inventory inventory, ItemStack item, int slot){
        inventory.setItem(slot, item);
        return inventory;
    }

        /*

      ALL FUNCTIONS BELOW ARE FOR HASHMAP OF MERCHANT INVENTORIES , NOT INVENTORIES THEMSELVES

     */

    public void setInvMap(UUID uuid, Inventory inv){
        Random rand = new Random();
        int itemNum = rand.nextInt(6) + 1;
        addItem(inv, getCustomTradeItems(itemNum), 10);
        itemNum = rand.nextInt(10) + 1;
        addItem(inv, getMinecraftTradeItems(itemNum), 12);
        itemNum = rand.nextInt(10) + 1;
        addItem(inv, getMinecraftTradeItems(itemNum), 14);
        itemNum = rand.nextInt(10) + 1;
        addItem(inv, getMinecraftTradeItems(itemNum), 16);
        itemNum = rand.nextInt(10) + 1;
        addItem(inv, getMinecraftTradeItems(itemNum), 28);
        itemNum = rand.nextInt(10) + 1;
        addItem(inv, getMinecraftTradeItems(itemNum), 30);
        itemNum = rand.nextInt(10) + 1;
        addItem(inv, getMinecraftTradeItems(itemNum), 32);
        itemNum = rand.nextInt(10) + 1;
        addItem(inv, getMinecraftTradeItems(itemNum), 34);
        addFrame(inv);
        addCloseButton(inv, 49);

        merchantList.put(uuid, inv);
    }

    private void addCloseButton(Inventory inv, int i) {
            ItemStack item = new ItemStack(Material.BARRIER);
            ItemMeta meta = item.getItemMeta();
            meta.setUnbreakable(false);
            meta.setDisplayName(ChatColor.RED + "Close Shop");
            item.setItemMeta(meta);
        addItem(inv, item, 49);
    }

    public Map<UUID, Inventory> getMerchantUUIDMap(){
        return merchantList;
    }

    private void addFrame(Inventory inv) {
        ItemStack frame = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta frameMeta = frame.getItemMeta();
        frameMeta.setDisplayName(ChatColor.DARK_GRAY+ " ");
        frame.setItemMeta(frameMeta);

        for(int i : new int[] {0,1,2,3,4,5,6,7,8,9,11,13,15,17,18,19,20,21,22,23,24,25,26,27,29,
                               31,33,35,36,37,38,39,40,41,42,43,44,45,46,47,48,50,51,52,53}){
            inv.setItem(i, frame);
        }

    }

    public Inventory getInventoryFromMap(UUID uuid){
        return merchantList.get(uuid);
    }

    /*

      ALL FUNCTIONS BELOW ARE FOR ITEMS BEING ADDED TO THE INVENTORIES, NOT INVENTORIES THEMSELVES

     */
    public ItemStack getCustomTradeItems(int roll){
        if (roll == 1) {
            //Grappling hook
            return grapplingHook.getHook();

        } else if (roll == 2) {
            //Emerald Blade
            return swords.getTheEmeraldDagger();
        } else if (roll == 3){
            //Moon Shards
            return lunarArmor.getMoonShards(Main.rollNumber(1,8));
        } else if (roll == 4){
            int armorRoll = Main.rollNumber(1,4);

            if (armorRoll == 1) {
                return peacesSymphony.getPeaceHelmet();
            } else if (roll == 2) {
                return peacesSymphony.getPeaceChestplate();
            } else if (roll == 3) {
                return peacesSymphony.getPeaceLeggings();
            } else if (roll == 4){
                return peacesSymphony.getPeaceBoots();
            }
        } else if (roll == 5){
            //Bagel
            return bagel.getThatBagel();
        } else if (roll == 6){
            //Smurf Handy Tool
            return pickaxes.giveHandyToolPickaxe();
        } else {
            Bukkit.getLogger().log(Level.WARNING,"Please give a value 1<=x<=6!, Value being given: " + roll);
        }
        return lunarArmor.getMoonShards(Main.rollNumber(1,5));
    }

    public ItemStack getMinecraftTradeItems(int roll){
        switch (roll) {
                case 1:
                    ItemStack item0 = new ItemStack(Material.ROTTEN_FLESH);
                    ItemMeta meta0 = item0.getItemMeta();
                    item0.setItemMeta(meta0);
                    meta0.setLore(Arrays.asList(
                            ChatColor.GRAY +  "To buy this, it costs",
                            ChatColor.AQUA + "8 Emeralds"));
                    item0.setItemMeta(meta0);
                    return item0;
                case 2:
                    ItemStack item1 = new ItemStack(Material.POISONOUS_POTATO);
                    ItemMeta meta1 = item1.getItemMeta();
                    item1.setItemMeta(meta1);
                    meta1.setLore(Arrays.asList(
                            ChatColor.GRAY +  "To buy this, it costs",
                            ChatColor.AQUA + "14 Carrots"));
                    item1.setItemMeta(meta1);
                    return item1;
                case 3:
                    // Code for case 3
                    return new ItemStack(Material.COBWEB, Main.rollNumber(1,10));
                case 4:
                    // Code for case 4
                    return new ItemStack(Material.DEAD_BUSH, Main.rollNumber(1,10));
                case 5:
                    // Code for case 5
                    return new ItemStack(Material.INK_SAC, Main.rollNumber(1,10));
                case 6:
                    // Code for case 6
                    return new ItemStack(Material.FEATHER, Main.rollNumber(1,10));
                case 7:
                    // Code for case 7
                    return new ItemStack(Material.PUFFERFISH, Main.rollNumber(1,10));
                case 8:
                    // Code for case 8
                    return new ItemStack(Material.SNOWBALL, Main.rollNumber(1,10));
                case 9:
                    // Code for case 9
                    return new ItemStack(Material.RED_MUSHROOM, Main.rollNumber(1,10));
                case 10:
                    // Code for case 10
                    return new ItemStack(Material.WOODEN_HOE);
                case 11:
                    // Code for case 11
                    return new ItemStack(Material.LEATHER_LEGGINGS);
                case 12:
                    // Code for case 12
                    return new ItemStack(Material.STONE_AXE);
                case 13:
                    // Code for case 13
                    return new ItemStack(Material.WOODEN_PICKAXE);
                case 14:
                    // Code for case 14
                    return new ItemStack(Material.GOLDEN_CARROT, Main.rollNumber(2,15));
                case 15:
                    // Code for case 15
                    return new ItemStack(Material.ENDER_EYE, Main.rollNumber(1,5));
                case 16:
                    // Code for case 16
                    return new ItemStack(Material.SHIELD);
                case 17:
                    // Code for case 17
                    return new ItemStack(Material.BOW);
                case 18:
                    // Code for case 18
                    return new ItemStack(Material.ARROW, Main.rollNumber(10,32));
                case 19:
                    // Code for case 19
                    return new ItemStack(Material.IRON_AXE);
                case 20:
                    // Code for case 20
                    return new ItemStack(Material.NETHER_WART, Main.rollNumber(1,5));
            default:
                Bukkit.getLogger().log(Level.WARNING, "Please give a value 1<=x<=20!");
                break;
        }
        return new ItemStack(Material.EMERALD, Main.rollNumber(1,5));
    }

    /*
    Wooden Shovel
    Leather Pants
    Wooden Hoe
    Dirt Block
    Red Mushroom
    Pufferfish
    Snowball

    Stone Sword
    Golden Carrot
    Eye of Ender
    Bucket of Water
    Shield
    Splash Potion of Healing
    Bow (without enchantments)
     */

}
