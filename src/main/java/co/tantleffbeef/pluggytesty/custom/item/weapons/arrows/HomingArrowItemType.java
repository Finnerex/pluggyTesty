package co.tantleffbeef.pluggytesty.custom.item.weapons.arrows;

import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class HomingArrowItemType extends SimpleItemType implements CustomArrow {

    private final Plugin schedulerPlugin;

    public HomingArrowItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.TIPPED_ARROW);
        this.schedulerPlugin = namespace;
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);
        meta.setLore(List.of(ChatColor.DARK_GREEN + "Seeks nearest enemy"));
        ((PotionMeta) meta).setColor(Color.fromRGB(21, 138, 122));
    }

    @Override
    public void applySpawnEffects(Arrow arrow) {
        Entity shooter = null;
        if (arrow.getShooter() instanceof Entity)
            shooter = (Entity) arrow.getShooter();


        final Entity finalShooter = shooter;
        BukkitRunnable runnable = new BukkitRunnable() {

            @Override
            public void run() {

                if(arrow.getVelocity().length() < 0.5f || arrow.isDead() || arrow.isInBlock()) {
                    cancel();
                    return;
                }

                // get the nearest entity that is not the shooter
                final Damageable target = getNearestEntity(arrow.getLocation(), finalShooter);

                if (target == null)
                    return;

                final Location targetLocation = target.getLocation().add(0, 0.7, 0);
                final Location arrowLocation = arrow.getLocation();

                final Vector direction = targetLocation.clone().toVector().subtract(arrowLocation.toVector()).normalize();

                arrow.setVelocity(direction.multiply(arrow.getVelocity().length()));

            }
        };

        runnable.runTaskTimer(schedulerPlugin, 0,3);

    }

    // gets the nearest entity that is not in the exclude list
    private Damageable getNearestEntity(Location l, Entity exclude) {

        Collection<Entity> entities = l.getWorld().getNearbyEntities(l, 5, 5, 5);

        Damageable closest = null;
        double closestDist = -1;

        for (Entity e : entities) {
            double d = e.getLocation().distance(l);
            if (e instanceof Damageable damageable && (closestDist == -1 || d < closestDist) && !e.equals(exclude)) {
                closestDist = d;
                closest = damageable;
            }
        }

        return closest;
    }

    @Override
    public void applyLandingEffects(Arrow arrow, ProjectileHitEvent event) {
        if (event.getHitEntity() == null)
            return;

        arrow.remove();
    }
}
