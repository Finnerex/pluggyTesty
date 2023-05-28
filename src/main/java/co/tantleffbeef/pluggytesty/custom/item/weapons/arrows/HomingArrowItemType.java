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

        // get the nearest entity that is not the shooter or the arrow (won't be the arrow anyway because not damageable)
        final Damageable target = getNearestEntity(arrow.getLocation(), Arrays.asList(shooter, arrow));

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if(arrow.getVelocity().length() < 0.2f) {
                    cancel();
                    return;
                }

                final Location targetLocation = target.getLocation();
                final Location arrowLocation = arrow.getLocation();

                // should be the yaw from the arrow to the target
                float yaw = (float) (Math.atan2(arrowLocation.getZ() - targetLocation.getZ(), arrowLocation.getX() - targetLocation.getX()) + 45);

                arrow.setRotation(arrowLocation.getYaw() + yaw / 10, arrowLocation.getPitch());

                Bukkit.broadcastMessage("yaw: " + yaw);

            }
        };

        runnable.runTaskTimer(schedulerPlugin, 10,1);

    }

    // gets the nearest entity that is not in the exclude list
    private Damageable getNearestEntity(Location l, List<Entity> exclude) {

        Collection<Entity> entities = l.getWorld().getNearbyEntities(l, 20, 20, 20);

        Damageable closest = null;
        double closestDist = -1;

        for (Entity e : entities) {
            double d = e.getLocation().distance(l);
            if (e instanceof Damageable damageable && (closestDist == -1 || d < closestDist) && !exclude.contains(e)) {
                closestDist = d;
                closest = damageable;
            }
        }

        return closest;
    }

    @Override
    public void applyLandingEffects(Arrow arrow, ProjectileHitEvent event) {/* no landing effects */}
}
