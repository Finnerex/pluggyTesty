package co.tantleffbeef.pluggytesty;

import org.bukkit.*;
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
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Random;

import java.util.ArrayList;

public class BossJawn implements CommandExecutor {
    private final Plugin plugin;
    public BossJawn(Plugin plugin) { this.plugin = plugin; }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player))
            return false;

        // spawn jawn with name
        Zombie jawn = (Zombie) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
        jawn.setCustomName(ChatColor.DARK_RED + "Jawn The Almighty");
        jawn.setCustomNameVisible(true);

        World w = jawn.getWorld();

        // armor
        EntityEquipment equipment = jawn.getEquipment();
        equipment.setChestplate(new ItemStack(Material.IRON_CHESTPLATE)); // my nuts may produce 'NullPointerException'
        equipment.setHelmet(new ItemStack(Material.NETHERITE_HELMET));
        // I guess I don't have to update inventory or nothin

        // ai / attacks??
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                Location l = jawn.getEyeLocation();

                if (jawn.isDead()) {
                    jawn.getWorld().playSound(l, Sound.ENTITY_WITHER_DEATH, 20, 0.1f);
                    cancel();
                    return;
                }

                Entity target = jawn.getTarget();

                Vector d = l.getDirection();

                if(target == null)
                    return;

                int attack = new Random().nextInt(5);
                Location targetLocation = target.getLocation();

                if (attack == 1 && jawn.hasLineOfSight(target) && l.distance(target.getLocation()) > 4) { // dash
                    jawn.setVelocity(d.multiply(3));
                }

                if (attack == 2) { // invis
                    jawn.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 40, 1));
                    jawn.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 3));
                }

                if (attack == 3 && l.distance(target.getLocation()) < 5) { // strength
                    jawn.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 30, 3));
                    jawn.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30 , 2));
                    w.playSound(l, Sound.BLOCK_NOTE_BLOCK_HARP, 5, 0.1f);
                }

                if (attack == 4) { // quake
                    quake(jawn.getLocation());
                }

            }
        };

        // run that jawn
        runnable.runTaskTimer(plugin, 0, 30);

        return true;
    }

    private void quake(Location location) {
        BukkitRunnable runnable = new BukkitRunnable() {
            int runs = 0;
            Location l = location;
            final BlockData blockParticle = Material.STONE.createBlockData();

            @Override
            public void run() {
                if (runs > 20) {
                    cancel();
                    return;
                }

                World w = l.getWorld();
                Vector d = l.getDirection().normalize();
                Vector pd = d.clone().rotateAroundY(90);
                Location l2 = l.clone().add(pd.clone().multiply(-3));

                if (w == null)
                    return;

                w.playSound(l, Sound.BLOCK_COMPOSTER_FILL, 8, 0.1f);

                for (int i = 0; i <= 6; i++) {
                    l2.add(pd.normalize());

                    w.spawnFallingBlock(l2, Material.BEACON.createBlockData());
                    w.spawnParticle(Particle.BLOCK_DUST, l2, 4, blockParticle);

                    ArrayList<Entity> entities = (ArrayList<Entity>) w.getNearbyEntities(l2, 1, 3, 1); // 1b side, 2b height
                    for (Entity e : entities) {
                        if (!(e instanceof Zombie) && e instanceof Damageable damageable)
                            damageable.damage(4);
                    }

                }

                l = l.add(d);

                runs++;
            }
        };

        runnable.runTaskTimer(plugin, 0, 1);
    }

}
