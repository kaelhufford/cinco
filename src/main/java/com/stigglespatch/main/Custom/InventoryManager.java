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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
        int itemNum = rand.nextInt(10) + 1;

        addItem(inv, getCustomTradeItems(itemNum), 10);
        addItem(inv, getMinecraftTradeItems(itemNum), 12);
        addItem(inv, getMinecraftTradeItems(itemNum), 14);
        addItem(inv, getMinecraftTradeItems(itemNum), 16);
        addItem(inv, getMinecraftTradeItems(itemNum), 28);
        addItem(inv, getMinecraftTradeItems(itemNum), 30);
        addItem(inv, getMinecraftTradeItems(itemNum), 34);

        merchantList.put(uuid, inv);
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
            return lunarArmor.getMoonShards();
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
            Bukkit.getLogger().log(Level.WARNING,"Please give a value 1<=x<=6!");
        }
        return new ItemStack(Material.AIR);
    }

    public ItemStack getMinecraftTradeItems(int roll){
        switch (roll) {
                case 1:
                    return new ItemStack(Material.ROTTEN_FLESH, Main.rollNumber(1,10));
                case 2:
                    // Code for case 2
                    return new ItemStack(Material.POISONOUS_POTATO, Main.rollNumber(1,10));
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
                    break;
                case 8:
                    // Code for case 8
                    break;
                case 9:
                    // Code for case 9
                    break;
                case 10:
                    // Code for case 10
                    break;
                case 11:
                    // Code for case 11
                    break;
                case 12:
                    // Code for case 12
                    break;
                case 13:
                    // Code for case 13
                    break;
                case 14:
                    // Code for case 14
                    break;
                case 15:
                    // Code for case 15
                    break;
                case 16:
                    // Code for case 16
                    break;
                case 17:
                    // Code for case 17
                    break;
                case 18:
                    // Code for case 18
                    break;
                case 19:
                    // Code for case 19
                    break;
                case 20:
                    // Code for case 20
                    break;
            default:
                Bukkit.getLogger().log(Level.WARNING, "Please give a value 1<=x<=20!");
                break;
        }
        return new ItemStack(Material.AIR);
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
