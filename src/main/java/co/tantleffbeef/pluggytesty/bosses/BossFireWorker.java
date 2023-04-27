package co.tantleffbeef.pluggytesty.bosses;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import java.util.Random;

public class BossFireWorker implements CommandExecutor {
    private final Plugin plugin;

    public BossFireWorker(Plugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player))
            return false;

        World w = player.getWorld();
        Location l = player.getEyeLocation();

        Creeper bouncer = (Creeper) w.spawnEntity(l, EntityType.CREEPER);
        bouncer.setPowered(true);

        bouncer.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5);
        bouncer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1000);
        bouncer.setHealth(1000);

        bouncer.setCustomName(ChatColor.GREEN + "BOUNCER");
        bouncer.setCustomNameVisible(true);

        BukkitRunnable runnable = new BukkitRunnable() {

            @Override
            public void run() {

                if (bouncer.isDead()) {
                    cancel();
                    return;
                }

                int attack = new Random().nextInt(10);

                if (attack == 0)
                    workFires(bouncer);
                if (attack == 1)
                    spreadWorks(bouncer);
                if (attack == 2) {
                    double jump = new Random().nextDouble(2);
                    bouncer.setVelocity(bouncer.getVelocity().add(new Vector(0, jump, 0)));
                }

            }
        };

        runnable.runTaskTimer(plugin, 0, 10);

        return true;
    }

    private void workFires(Creeper bouncer) {

        BukkitRunnable runnable = new BukkitRunnable() {

            int runs = 0;

            @Override
            public void run() {

                if (runs > 9) {
                    cancel();
                    return;
                }

                Location loc = bouncer.getEyeLocation();

                Firework f = (Firework) bouncer.getWorld().spawnEntity(loc, EntityType.FIREWORK);
                f.setShotAtAngle(true);
                f.getLocation().setDirection(loc.getDirection().normalize());
                f.setVelocity(loc.getDirection().multiply(2));
                f.setBounce(true);
                f.setShooter(bouncer);

                FireworkMeta meta = f.getFireworkMeta();

                meta.addEffect(buildFirework());
                meta.setPower(4);

                f.setFireworkMeta(meta);

                runs ++;

            }
        };

        runnable.runTaskTimer(plugin, 0, 5);

    }

    private FireworkEffect buildFirework() {
        Random r = new Random();
        FireworkEffect.Builder builder = FireworkEffect.builder();

        builder.flicker(r.nextBoolean());
        builder.trail(r.nextBoolean());
        builder.withColor(Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
        builder.withFade(Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256)));
        builder.with(FireworkEffect.Type.values()[r.nextInt(5)]);

        return builder.build();
    }

    private void spreadWorks(Creeper bouncer) {

        BukkitRunnable runnable = new BukkitRunnable() {

            int runs = 0;
            Location l = bouncer.getEyeLocation();

            @Override
            public void run() {

                if (runs > 6) {
                    cancel();
                    return;
                }

                Firework f = (Firework) bouncer.getWorld().spawnEntity(l, EntityType.FIREWORK);
                f.setShotAtAngle(true);
                f.getLocation().setDirection(l.getDirection().normalize());
                f.setVelocity(l.getDirection().multiply(2));
                f.setBounce(true);
                f.setShooter(bouncer);

                FireworkMeta meta = f.getFireworkMeta();

                meta.addEffect(buildFirework());
                meta.setPower(4);

                f.setFireworkMeta(meta);

                l.setYaw(l.getYaw() + 45);

                runs++;

            }

        };

        runnable.runTaskTimer(plugin, 0, 5);

    }
}
