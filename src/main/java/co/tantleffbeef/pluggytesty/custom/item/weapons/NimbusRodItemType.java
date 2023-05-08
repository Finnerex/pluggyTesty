package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Random;

public class NimbusRodItemType extends SimpleItemType implements InteractableItemType {

    private int num = 0;
    private int greatestRuns = 600;
    private final Plugin schedulerPlugin;

    public NimbusRodItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.DIAMOND_HOE);
        this.schedulerPlugin = namespace;
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {
        if (player.hasCooldown(Material.DIAMOND_HOE))
            return false;

        num ++;

        final Location location;

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
                if (runs >= greatestRuns)
                    greatestRuns = runs;

                if (runs >= 600 || num > 2 && runs == greatestRuns) {
                    num --;
                    greatestRuns = 0;
                    cancel();
                    return;
                }

                Random r = new Random();

                if (runs % 50 == 0) { // 5 secs respawn cloud

                    for (int i = 0; i < 10; i ++) {
                        w.spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, location.clone().add( // goofy way to set velocity of that particle
                                r.nextDouble(-0.8, 0.8), r.nextDouble(0.8), r.nextDouble(-0.8, 0.8)), 2, 0, 0, 0, 0);
                    }
                }

                if (runs % 10 == 0) { // damage every second
                    Collection<Entity> entities = w.getNearbyEntities(location.clone().subtract(0, 15, 0), 1, 14, 1);
                    for (Entity e : entities) {
                        if (e instanceof Damageable d) {
                            d.damage(3, player);
                        }
                    }
                }

                w.spawnParticle(Particle.FALLING_WATER, location.clone().add(
                        r.nextDouble(-0.8, 0.8), 0, r.nextDouble(-0.8, 0.8)), 1);

                runs ++;
            }
        };

        runnable.runTaskTimer(schedulerPlugin, 0, 1);


        return false;
    }
}
