package com.stigglespatch.main.Custom.Enchants;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Smelter extends Enchantment implements Listener {

    public Smelter() {
        super(NamespacedKey.minecraft("auto_smelter"));
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        if (!e.isDropItems()) return;
        if (e.getPlayer().getInventory().getItemInMainHand() == null) return;
        if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasEnchant(this)) {
            if (e.getBlock().getType().equals(Material.STONE)) {
                e.setDropItems(false);
                e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.STONE));

            } else if (e.getBlock().getType().equals(Material.IRON_ORE)) {
                e.setDropItems(false);
                e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.IRON_INGOT));

            } else if (e.getBlock().getType().equals(Material.COPPER_ORE)) {
                e.setDropItems(false);
                e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.COPPER_INGOT));

            } else if (e.getBlock().getType().equals(Material.GOLD_ORE)) {
                e.setDropItems(false);
                e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.GOLD_INGOT));

            } else if (e.getBlock().getType().equals(Material.GRANITE)) {
                e.setDropItems(false);
                e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.POLISHED_GRANITE));

            } else if (e.getBlock().getType().equals(Material.ANDESITE)) {
                e.setDropItems(false);
                e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.POLISHED_ANDESITE));

            }
        }
    }

    @Override
    public String getName() {
        return "Auto Smelter";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true;
    }
}