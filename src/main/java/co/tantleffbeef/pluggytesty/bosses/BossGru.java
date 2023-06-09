package co.tantleffbeef.pluggytesty.bosses;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Random;

import java.util.ArrayList;

public class BossGru implements CommandExecutor {

    private final Plugin plugin;
    public BossGru(Plugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player))
            return false;

        Villager gru = player.getWorld().spawn(player.getLocation(), Villager.class, (villager) -> {
            villager.setProfession(Villager.Profession.ARMORER);
            villager.setCustomName(ChatColor.LIGHT_PURPLE + "GRUUUUUUU");
            villager.setCustomNameVisible(true);
            villager.setPersistent(true);

            villager.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(2047);
            villager.setHealth(2047);
        });

        final World w = gru.getWorld();


        BukkitRunnable runnable = new BukkitRunnable() {

            double prevHealth = 2047;
            double currHealth = 2047;
            @Override
            public void run() {

                Location eye = gru.getEyeLocation();

                if (gru.isDead()) { //dead command
                    for (int i = 0; i < 15; i++) {

                        ((Zombie) w.spawnEntity(eye, EntityType.ZOMBIE)).setBaby();

                    }
                    w.playSound(eye, Sound.ENTITY_PLAYER_LEVELUP, 20, 0.1f);

                    cancel();
                    return;
                }

                currHealth = gru.getHealth();


                if (currHealth < prevHealth){ //spawns zombie hoard on hit

                    for (int i = 0; i < 5; i++){

                        int rand = new Random().nextInt(10) - 5;
                        Location random = new Location(w, eye.getX() + rand, eye.getY(), eye.getZ() + rand);

                        Zombie swordZombie = w.spawn(random, Zombie.class, (zombie) -> {
                            zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(1);

                            EntityEquipment equipment = zombie.getEquipment();
                            equipment.setItemInMainHand(new ItemStack(Material.IRON_SWORD));
                            equipment.setHelmet(new ItemStack(Material.LEATHER_HELMET));
                            equipment.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                            equipment.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                            equipment.setBoots(new ItemStack(Material.LEATHER_BOOTS));

                        });

                        LivingEntity target = getNearestEntity(eye, swordZombie);
                        if (target instanceof Player)
                            swordZombie.setTarget(target);

                    }

                    prevHealth = currHealth;
                }

            }
        };
        runnable.runTaskTimer(plugin, 0, 1);

        return true;
    }

    private LivingEntity getNearestEntity(Location l, Entity entity) {

        Collection<Entity> entities = entity.getNearbyEntities(50, 10, 50);

        LivingEntity closest = null;
        double closestDist = -1;

        for (Entity e : entities) {
            double d = e.getLocation().distance(l);
            if (!(e instanceof Zombie) && !e.equals(entity) && e instanceof LivingEntity && (closestDist == -1 || d < closestDist)) {
                closestDist = d;
                closest = (LivingEntity) e;
            }
        }

        return closest;
    }


}
