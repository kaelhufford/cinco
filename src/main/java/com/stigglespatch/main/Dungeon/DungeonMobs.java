package com.stigglespatch.main.Dungeon;

import com.stigglespatch.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DungeonMobs {

    static Main plugin;
    public DungeonMobs(Main plugin){
        this.plugin = plugin;
    }

    public static void spawnDungeonMobs (Location location, String type) {
        if (type.equals("zombie"))
            spawnDungeonZombie(location);
        else if (type.equals("skeleton"))
            spawnDungeonSkeleton(location);
        else if (type.equals("creeper"))
            spawnDungeonCreeper(location);
    }
    public static void spawnDungeonZombie(Location location){
        Zombie zombie = location.getWorld().spawn(location, Zombie.class);
        zombie.setCustomName(ChatColor.RED + "Dungeon Zombie");
        zombie.setCustomNameVisible(true);

        Attributable zombieAt = zombie;
        AttributeInstance attributeDamage = zombieAt.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        AttributeInstance attributeHealth = zombieAt.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attributeDamage.setBaseValue(10);
        attributeHealth.setBaseValue(20);
        zombie.setHealth(10);

        new BukkitRunnable(){
            public void run(){
                if(!zombie.isDead()) {
                    if(zombie.getTarget() == null){
                        for (Entity entity : zombie.getNearbyEntities(10,10,10)){
                            if (entity instanceof Player){
                                Player p = (Player) entity;
                                zombie.setTarget(p);
                            }
                        }
                    }

                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 10);
    }

    public static void spawnDungeonSkeleton(Location location){
        Skeleton mob = location.getWorld().spawn(location, Skeleton.class);
        mob.setCustomName(ChatColor.RED + "Dungeon Skeleton");
        mob.setCustomNameVisible(true);
        Attributable mobAt = mob;
        AttributeInstance attributeDamage = mobAt.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        AttributeInstance attributeHealth = mobAt.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attributeDamage.setBaseValue(10);
        attributeHealth.setBaseValue(25);
        mob.setHealth(10);

        new BukkitRunnable(){
            public void run(){
                if(!mob.isDead()) {
                    if(mob.getTarget() == null){
                        for (Entity entity : mob.getNearbyEntities(20,20,20)){
                            if (entity instanceof Player){
                                Player p = (Player) entity;
                                mob.setTarget(p);
                            }
                        }
                    }

                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 10);
    }

    public static void spawnDungeonCreeper(Location location){
        Creeper mob = location.getWorld().spawn(location, Creeper.class);
        mob.setCustomName(ChatColor.RED + "Dungeon Creeper");
        mob.setCustomNameVisible(true);
        Attributable mobAt = mob;
        AttributeInstance attributeDamage = mobAt.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        AttributeInstance attributeHealth = mobAt.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attributeDamage.setBaseValue(20);
        attributeHealth.setBaseValue(25);
        mob.setHealth(20);


        new BukkitRunnable(){
            public void run(){
                if(!mob.isDead()) {
                    if(mob.getTarget() == null){
                        for (Entity entity : mob.getNearbyEntities(20,20,20)){
                            if (entity instanceof Player){
                                Player p = (Player) entity;
                                mob.setTarget(p);
                            }
                        }
                        for (Entity entity : mob.getNearbyEntities(5,5,5)){
                            if (entity instanceof Player){
                                Player p = (Player) entity;
                                mob.setTarget(p);
                            }
                        }
                    }

                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 10);
    }

    public static Entity spawnDungeonBoss(Location location){
        String world = location.getWorld().getName();
        Zombie boss = location.getWorld().spawn(location, Zombie.class);
        boss.setCustomName(ChatColor.GOLD + "Knight of the Dungeons");
        boss.setCustomNameVisible(true);
        Attributable bossAt = boss;
        AttributeInstance attributeHealth = bossAt.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attributeHealth.setBaseValue(56);
        boss.setHealth(56);
        AttributeInstance attributeAttack = bossAt.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        attributeAttack.setBaseValue(40);
        boss.setSeed(1);

        new BukkitRunnable(){
            public void run(){
                if(!boss.isDead()) {
                    if(boss.getTarget() == null){
                        for (Entity entity : boss.getNearbyEntities(4,4,4)){
                            if (entity instanceof Player){
                                Player p = (Player) entity;
                                if (DungeonManager.getAlivePlayers(world).contains(p))
                                    boss.setTarget(p);
                            }
                        }

                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 10);

        return boss;
/*
        new BukkitRunnable(){
            public void run(){
                if(!boss.isDead()) {
                    if(boss.getTarget() == null){
                        for (Entity entity : boss.getNearbyEntities(40,40,40)){
                            if (entity instanceof Player){
                                Player p = (Player) entity;
                                boss.setTarget(p);
                            }
                        }
                        for (Entity entity : boss.getNearbyEntities(5,5,5)){
                            if (entity instanceof Player){
                                Player p = (Player) entity;
                                boss.setTarget(p);
                            }
                        }
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 10);*/
    }

}
