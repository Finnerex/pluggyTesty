package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;


public class MagnetSphereItemType extends SimpleItemType implements InteractableItemType {

    private final Plugin schedulerPlugin;

    public MagnetSphereItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.ENCHANTED_BOOK);
        this.schedulerPlugin = namespace;
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {

        final World w = player.getWorld();
        final Location location = player.getEyeLocation();
        final Vector direction = location.getDirection();


        // blue glass with lantern inside
        final ItemDisplay glass = w.spawn(location, ItemDisplay.class, (display) -> {
            display.setItemStack(new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS));
        });

        final ItemDisplay lantern = w.spawn(location, ItemDisplay.class, inside -> {
            inside.setItemStack(new ItemStack(Material.SEA_LANTERN));
            // make smaller
            Transformation transformation = inside.getTransformation();
            transformation.getScale().set(0.35);
            inside.setTransformation(transformation);
        });


        BukkitRunnable runnable = new BukkitRunnable() {

            int runs = 0; // period 2 ticks

            @Override
            public void run() {

                if (runs > 150) { // remove after 15 secs
                    lantern.remove();
                    glass.remove();
                    cancel();
                    return;
                }

                // damage the closest entity and particles
                Damageable d = getNearestEntity(location, glass);
                if (d != null && !d.equals(player)) {
                    Bukkit.broadcastMessage(d.toString());
                    d.damage(6, player);
                    d.setVelocity(new Vector(0, 0, 0)); // stop that dawg in his tracks

                    final Location dLocation = d.getLocation();
                    final double dist = location.distance(dLocation);
                    final Vector dir = location.subtract(dLocation).toVector().normalize();
//                    for (double i = 0; i < dist; i += 0.1) {
//                        w.spawnParticle(Particle.BUBBLE_POP, dLocation.add(dir.clone().multiply(0.1)), 2, 0, 0, 0, 0);
//                    }
                }

                // teleport both
                final Location newPos = location.add(direction.clone().multiply(0.2));
                glass.teleport(newPos);
                lantern.teleport(newPos);

                runs ++;
            }
        };

        runnable.runTaskTimer(schedulerPlugin, 0, 2);

        return false;
    }

    private Damageable getNearestEntity(Location l, Entity entity) {

        Collection<Entity> entities = entity.getNearbyEntities(6, 6, 6);

        Damageable closest = null;
        double closestDist = -1;

        for (Entity e : entities) {
            double d = e.getLocation().distance(l);
            if (e instanceof Damageable damageable && (closestDist == -1 || d < closestDist)) {
                closestDist = d;
                closest = damageable;
            }
        }

        return closest;
    }
}
