package co.tantleffbeef.pluggytesty.bosses;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Random;

public class BossTrial5 implements CommandExecutor {
    private final Plugin plugin;
    public BossTrial5(Plugin plugin) { this.plugin = plugin; }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player))
            return false;

        // spawn jawn with name
        Zombie jawn = player.getWorld().spawn(player.getLocation(), Zombie.class, (zombie) -> {
            zombie.setCustomName(ChatColor.DARK_RED + "Jawn The Almighty");
            zombie.setCustomNameVisible(true);
            zombie.setPersistent(true);

            // armor
            EntityEquipment equipment = zombie.getEquipment();
            equipment.setChestplate(new ItemStack(Material.IRON_CHESTPLATE)); // my nuts may produce 'NullPointerException'
            equipment.setHelmet(new ItemStack(Material.NETHERITE_HELMET));
            // I guess I don't have to update inventory or nothin

            zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(300); // dont listen to it, null pointers are fake and made up by the Jet brains conspiracy
            zombie.setHealth(300);

        });

        final World w = jawn.getWorld();

        // ai / attacks?!?!
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

                int attack = new Random().nextInt(8);
                Location targetLocation = target.getLocation();

                if (attack == 1 && jawn.hasLineOfSight(target) && l.distance(targetLocation) > 4) { // dash
                    jawn.setVelocity(d.multiply(3));
                }

                if (attack == 2) { // invis
                    jawn.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 40, 1));
                    jawn.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 3));
                }

                if (attack == 3 && l.distance(targetLocation) < 5) { // strength
                    jawn.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 30, 3));
                    jawn.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 30 , 2));
                    w.playSound(l, Sound.BLOCK_NOTE_BLOCK_HARP, 5, 0.1f);
                }

                if (attack == 4 || attack == 5) { // quake
                    quake(jawn.getLocation(), jawn);
                }

            }
        };

        // run that jawn
        runnable.runTaskTimer(plugin, 0, 20);

        return true;
    }

    private void quake(Location location, Zombie jawn) {
        BukkitRunnable runnable = new BukkitRunnable() {
            int runs = 0;
            final Location l = location;
            final BlockData blockParticle = Material.STONE.createBlockData();

            @Override
            public void run() {
                if (runs > 20) {
                    cancel();
                    return;
                }

                final World w = l.getWorld();
                Vector d = l.getDirection().setY(0).normalize();
                Vector pd = d.clone().rotateAroundY(90);
                Location l2 = l.clone().add(pd.clone().multiply(-4));

                if (w == null)
                    return;

                w.playSound(l, Sound.BLOCK_COMPOSTER_FILL, 8, 0.1f);

                for (int i = 0; i < 7; i++) {
                    l2.add(pd); // should probably be normal

                    //w.spawnFallingBlock(l2, Material.BEACON.createBlockData());
                    w.spawnParticle(Particle.DUST, l2, 4, blockParticle);

                    Collection<Entity> entities = w.getNearbyEntities(l2, 0.5, 3, 0.5); // 1b side, 2b height
                    for (Entity e : entities) { // damage all entities in that block space
                        if (!(e instanceof Zombie) && e instanceof Damageable damageable)
                            damageable.damage(4, jawn);
                    }

                }

                l.add(d);
                runs++;
            }
        };

        runnable.runTaskTimer(plugin, 0, 2);
    }

}
