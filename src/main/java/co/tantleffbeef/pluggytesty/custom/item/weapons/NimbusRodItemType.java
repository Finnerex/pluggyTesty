package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class NimbusRodItemType extends SimpleItemType implements InteractableItemType {


    // stores the number of clouds spawned for each player
    private final Map<UUID, Integer> numClouds = new HashMap<>();

    // holds the greatest number of runs in any cloud per player
    // used to determine which should be deleted when the player attempts to spawn a 3rd cloud
    private final Map<UUID, Integer> greatestRuns = new HashMap<>();;
    private final Plugin schedulerPlugin;

    public NimbusRodItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.DIAMOND_HOE);
        this.schedulerPlugin = namespace;
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);
        meta.setLore(Arrays.asList(ChatColor.DARK_GREEN + "Right-Click : Damaging rain cloud", ChatColor.DARK_GREEN + "No Cooldown"));
        meta.setUnbreakable(true);
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {

        UUID uuid = player.getUniqueId();

        // init to no clouds and no runs
        numClouds.putIfAbsent(uuid, 0);
        greatestRuns.putIfAbsent(uuid, 0);

        // new cloud spawned
        numClouds.put(uuid, numClouds.get(uuid) + 1);

        final Location location;

        // should be spawned 15 blocks away or the closest block on that line
        RayTraceResult result = player.rayTraceBlocks(15);

        if (result != null && result.getHitBlock() != null)
            location = result.getHitBlock().getLocation();
        else
            location = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(15));


        final World w = player.getWorld();

        BukkitRunnable runnable = new BukkitRunnable() {

            int runs = 0; // 1 run 2 ticks : 10 runs per sec
            @Override
            public void run() {
                if (runs >= greatestRuns.get(uuid))
                    greatestRuns.put(uuid, runs);

                // remove if try to spawn 3rd and this has the most run time
                // or if time of 600 runs expires (should be 1 min)
                if (runs >= 600 || numClouds.get(uuid) > 2 && runs == greatestRuns.get(uuid)) {
                    numClouds.put(uuid, numClouds.get(uuid) - 1);
                    greatestRuns.put(uuid, 0);
                    cancel();
                    return;
                }

                Random r = new Random();

                if (runs % 75 == 0) { // 7.5 secs respawn cloud

                    for (int i = 0; i < 30; i ++) {
                        w.spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, location.clone().add( // goofy way to set velocity of that particle
                                r.nextDouble(-1.7, 1.7), r.nextDouble(0.6), r.nextDouble(-1.7, 1.7)), 2, 0, 0, 0, 0);
                    }
                }

                if (runs % 10 == 0) { // damage every second
                    Collection<Entity> entities = w.getNearbyEntities(location.clone().subtract(0, 15, 0), 3, 14, 3);
                    for (Entity e : entities) {
                        if (e instanceof Damageable d && !e.equals(player)) {
                            d.damage(3, player);
                        }
                    }
                }

                w.spawnParticle(Particle.FALLING_WATER, location.clone().add(
                        r.nextDouble(-1.7, 1.7), 0, r.nextDouble(-1.7, 1.7)), 1);

                runs ++;
            }
        };

        runnable.runTaskTimer(schedulerPlugin, 0, 2);

        return false;
    }
}
