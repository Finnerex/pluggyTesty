package co.tantleffbeef.pluggytesty.bosses;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.boss.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Random;

import java.util.ArrayList;

public class BossTrial1 implements CommandExecutor {
    private final Plugin plugin;
    public BossTrial1(Plugin plugin) { this.plugin = plugin; }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player))
            return false;

        // spawn boss with name
        ZombieVillager boss = player.getWorld().spawn(player.getLocation(), ZombieVillager.class, (zombie) -> {
            zombie.setCustomName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "The " + ChatColor.MAGIC + "Fallen" + ChatColor.RESET + "" + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + " Necromancer");
            zombie.setCustomNameVisible(true);
            zombie.setPersistent(true);

            EntityEquipment equipment = zombie.getEquipment();
            equipment.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
            equipment.setHelmet(new ItemStack(Material.NETHERITE_HELMET));

            zombie.setVillagerProfession(Villager.Profession.CLERIC);
            zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(300);
            zombie.setHealth(300);

        });

        final World world = boss.getWorld();

        BossBar bossBar = Bukkit.createBossBar(
                ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "The " + ChatColor.MAGIC + "Fallen" + ChatColor.RESET + "" + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + " Necromancer",
                BarColor.GREEN,
                BarStyle.SEGMENTED_6
        );
        bossBar.addPlayer(player);

        // summon the zombie and skeleton horse rider minions

        Location bossLoc = boss.getLocation();

        Chicken zombHorse = player.getWorld().spawn(new Location(player.getWorld(), bossLoc.getX() + 5, bossLoc.getY(), bossLoc.getZ()), Chicken.class, (horse) -> {
            // horse.setTamed(true);
        });
        Zombie zombRider = player.getWorld().spawn(new Location(player.getWorld(), bossLoc.getX() + 5, bossLoc.getY(), bossLoc.getZ()), Zombie.class, (zomb) ->{
            zomb.getEquipment().setHelmet(new ItemStack(Material.CARVED_PUMPKIN));
        });

        Spider skeleHorse = player.getWorld().spawn(new Location(player.getWorld(), bossLoc.getX() - 5, bossLoc.getY(), bossLoc.getZ()), Spider.class, (horse) ->{
            // horse.setTamed(true);
        });
        Skeleton skeleRider = player.getWorld().spawn(new Location(player.getWorld(), bossLoc.getX() - 5, bossLoc.getY(), bossLoc.getZ()), Skeleton.class, (skele) ->{
            skele.getEquipment().setHelmet(new ItemStack(Material.CARVED_PUMPKIN));
        });

        zombHorse.addPassenger(zombRider);
        skeleHorse.addPassenger(skeleRider);

        // attacks
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                Location l = boss.getEyeLocation();
                bossBar.setProgress(boss.getHealth() / boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());

                if (boss.isDead()) {
                    boss.getWorld().playSound(l, Sound.ENTITY_ENDERMAN_DEATH, 20, 0.1f);
                    bossBar.removeAll();
                    bossBar.setVisible(false);
                    cancel();
                    return;
                }


                Entity target = boss.getTarget();

                Vector d = l.getDirection();

                if(target == null)
                    return;

                int attack = new Random().nextInt(8);
                Location targetLocation = target.getLocation();

                if (attack == 1) { // dash

                }

                if (attack == 2) { // invis

                }

                if (attack == 3) { // strength

                }

                if (attack == 4 || attack == 5) { // quake

                }

            }
        };

        // run that jawn
        runnable.runTaskTimer(plugin, 0, 20);

        return true;
    }

}
