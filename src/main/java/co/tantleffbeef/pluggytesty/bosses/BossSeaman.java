package co.tantleffbeef.pluggytesty.bosses;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import java.util.Random;

import java.util.ArrayList;

public class BossSeaman implements CommandExecutor {
    private final Plugin plugin;
    public BossSeaman(Plugin plugin) { this.plugin = plugin; }
    boolean isDoingStuff = false;
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player))
            return false;

        // spawn seaman with name
        Drowned seaman = player.getWorld().spawn(player.getLocation(), Drowned.class, (drowned) -> {
            drowned.setCustomName(ChatColor.DARK_AQUA + "The Fallen Seaman");
            drowned.setCustomNameVisible(true);
            drowned.setPersistent(true);
            drowned.setAdult(); // hi

            EntityEquipment equipment = drowned.getEquipment();
            equipment.setHelmet(new ItemStack(Material.GOLDEN_HELMET));
            equipment.setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
            equipment.setLeggings(new ItemStack(Material.GOLDEN_LEGGINGS));
            equipment.setBoots(new ItemStack(Material.GOLDEN_BOOTS));
            equipment.setItemInMainHand(new ItemStack(Material.TRIDENT));

            drowned.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100);
            drowned.setHealth(100);
        });

        final World w = seaman.getWorld();


        // ai / attacks??
        BukkitRunnable runnable = new BukkitRunnable() {

            @Override
            public void run() {
                Location loc = seaman.getEyeLocation();
                Vector dir = loc.getDirection();
                Entity target = seaman.getTarget();

                w.setStorm(true);
                w.setThundering(true);
                w.setWeatherDuration(400);

                if (seaman.isDead()) {
                    w.setStorm(false);
                    w.setThundering(false);
                    w.setWeatherDuration(0);
                    cancel();
                    return;
                }

                if(target == null) return;

                if(isDoingStuff) return;

                for(Entity i : seaman.getNearbyEntities(20, 10, 20)) {
                    if(i.getType().equals(EntityType.TRIDENT) && i.getTicksLived() < 1000) {
                        i.setTicksLived(1100);
                    }
                }

                int attack = new Random().nextInt(8); // hi

                if (attack == 1 || seaman.getHealth() < 20) { // charge up and heal if not interrupted.
                    heal(loc, seaman, seaman.getHealth());
                } else if (attack == 2 && seaman.hasLineOfSight(target) && loc.distance(target.getLocation()) > 4) { // ride the trident
                    return;
                } else if (attack == 3 && loc.distance(target.getLocation()) < 5) { // lightning strikes him and supercharges him.
                    return;
                } else if (attack == 4) { // 8 tridents in 45 degree increments.
                    Vector summonDir = dir.clone();
                    for(int i = 0; i < 8; i++) {
                        w.spawn(loc, Trident.class, (trident) -> {
                            trident.setVelocity(summonDir.rotateAroundY(45).setY(30));
                        });
                    }
                } else if (attack == 5) { // Lightning strikes around him and summons normal drowned with turtle shells and speed.
                    Location summonHere = loc.clone().add(new Random().nextInt(10)-5, 0, new Random().nextInt(10)-5);
                    seaman.getWorld().playSound(summonHere, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1, 0.1f);

                    for(int i = 0; i < 4; i++) {
                        seaman.getWorld().spawnEntity(summonHere, EntityType.LIGHTNING, false);
                        w.spawn(summonHere, Drowned.class, (drowned) -> {
                            EntityEquipment mEquip = drowned.getEquipment();
                            mEquip.setHelmet(new ItemStack(Material.TURTLE_HELMET));
                        });
                    }
                }
            }
        };
        runnable.runTaskTimer(plugin, 0, 20);

        return true;
    }

    private void heal(Location location, Drowned seaman, double hp) {
        BukkitRunnable runnable = new BukkitRunnable() {
            int runs = 0;

            @Override
            public void run() {
                if(runs > 40) {
                    isDoingStuff = false;
                    seaman.getWorld().playSound(seaman, Sound.ENTITY_ZOMBIE_CONVERTED_TO_DROWNED, 1, 1);
                    cancel();
                }

                if(runs > 0 && seaman.getHealth() < hp) {
                    seaman.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, seaman.getLocation(), 1);
                    isDoingStuff = false;
                    cancel();
                }
                isDoingStuff = true;
                seaman.getWorld().spawnParticle(Particle.HEART, seaman.getLocation(), 3, 1.0, 0.0, 1.0);
                runs++;
            }
        };

        runnable.runTaskTimer(plugin, 0, 1);
    }

    private void rident(Location location, Drowned seaman) {
        BukkitRunnable runnable = new BukkitRunnable() {
            int runs = 0;

            @Override
            public void run() {
                if(runs > 40) {
                    cancel();
                }

                runs++;
            }
        };

        runnable.runTaskTimer(plugin, 0, 1);
    }

    private void supercharge(Location location, Drowned seaman) {
        BukkitRunnable runnable = new BukkitRunnable() {
            int runs = 0;

            @Override
            public void run() {
                if(runs > 40) {
                    cancel();
                }

                runs++;
            }
        };

        runnable.runTaskTimer(plugin, 0, 1);
    }

    private void octagon(Location location, Drowned seaman) {
//        BukkitRunnable runnable = new BukkitRunnable() {
//            int runs = 0;
//            Vector summonDir = location.getDirection().clone();
//            for(int i = 0; i < 8; i++) {
//                Trident creation = (Trident) seaman.getWorld().spawnEntity(location, EntityType.TRIDENT);
//                creation.setVelocity(summonDir.rotateAroundY(45).setY(30));
//            }
//            @Override
//            public void run() {
//                if(runs > 10) {
//
//                    cancel();
//                }
//                runs++;
//            }
//        };
//
//        runnable.runTaskTimer(plugin, 0, 20);
    }

}
