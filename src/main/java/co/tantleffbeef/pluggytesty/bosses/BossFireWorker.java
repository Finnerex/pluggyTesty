package co.tantleffbeef.pluggytesty.bosses;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
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

        final World w = player.getWorld();
        Location l = player.getEyeLocation();

        ZombieVillager bouncer = w.spawn(l, ZombieVillager.class, (zombieVil) -> {
            zombieVil.setVillagerType(Villager.Type.SWAMP);
            zombieVil.setVillagerProfession(Villager.Profession.CARTOGRAPHER);

            zombieVil.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100);
            zombieVil.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5);
            zombieVil.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1000);
            zombieVil.setHealth(1000);

            zombieVil.setPersistent(true);

            zombieVil.setCustomName(ChatColor.GREEN + "BOUNCER");
            zombieVil.setCustomNameVisible(true);
        });

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
                if (attack >= 8) {
                    double jump = new Random().nextDouble(2);
                    bouncer.setVelocity(bouncer.getVelocity().add(new Vector(0, jump, 0)));
                }

                int text = new Random().nextInt(500);

                if (text == 1)
                    Bukkit.broadcastMessage(ChatColor.GREEN + "<Bouncer> 218 Goodale Road");
                if (text == 2)
                    Bukkit.broadcastMessage(ChatColor.GREEN + "<Bouncer> Happy Birthday gavvy wavvy");
                if (text == 3)
                    // This is allowed because Susan made it
                    Bukkit.broadcastMessage(ChatColor.GREEN + "<Bouncer> Homosexual");
                if (text == 4)
                    Bukkit.broadcastMessage(ChatColor.GREEN + "<Bouncer> Congartulations");
                if (text == 5)
                    Bukkit.broadcastMessage(ChatColor.GREEN + "<Bouncer> This sounds like the song of the summer");


            }
        };

        runnable.runTaskTimer(plugin, 0, 10);

        return true;
    }

    private void workFires(LivingEntity bouncer) {

        BukkitRunnable runnable = new BukkitRunnable() {

            int runs = 0;

            @Override
            public void run() {

                if (runs > 5) {
                    cancel();
                    return;
                }

                Location loc = bouncer.getEyeLocation();

                bouncer.getWorld().spawn(loc, Firework.class, (f) -> {
                    f.setShotAtAngle(true);
                    f.getLocation().setDirection(loc.getDirection().normalize());
                    f.setVelocity(loc.getDirection().multiply(2));
                    f.setBounce(true);
                    f.setShooter(bouncer);

                    FireworkMeta meta = f.getFireworkMeta();

                    meta.addEffect(buildFirework());
                    meta.setPower(4);

                    f.setFireworkMeta(meta);
                });

                loc.getWorld().playSound(loc, Sound.ITEM_CROSSBOW_SHOOT, 1, 1);

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

    private void spreadWorks(LivingEntity bouncer) {

        BukkitRunnable runnable = new BukkitRunnable() {

            int runs = 0;
            Location loc = bouncer.getEyeLocation();

            @Override
            public void run() {

                if (runs > 7) {
                    cancel();
                    return;
                }

                bouncer.getWorld().spawn(loc, Firework.class, (f) -> {
                    f.setShotAtAngle(true);
                    f.getLocation().setDirection(loc.getDirection().normalize());
                    f.setVelocity(loc.getDirection().multiply(2));
                    f.setBounce(true);
                    f.setShooter(bouncer);

                    FireworkMeta meta = f.getFireworkMeta();

                    meta.addEffect(buildFirework());
                    meta.setPower(4);

                    f.setFireworkMeta(meta);
                });

                loc.setYaw(loc.getYaw() + 45);

                loc.getWorld().playSound(loc, Sound.BLOCK_DISPENSER_DISPENSE, 1, 1);

                runs++;

            }

        };

        runnable.runTaskTimer(plugin, 0, 5);

    }
}
