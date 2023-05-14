package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;

public class MeowItemType extends SimpleItemType implements InteractableItemType {

    private final Plugin schedulerPlugin;

    public MeowItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.NETHERITE_SWORD);
        this.schedulerPlugin = namespace;
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {


        ItemDisplay projectile = player.getWorld().spawn(player.getEyeLocation(), ItemDisplay.class, (proj) -> { // Creates a Conduit projectile and sets its velocity.
            proj.setItemStack(new ItemStack(Material.CONDUIT));
        });

        projectile.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(5));


        RayTraceResult result = player.getWorld().rayTraceBlocks(player.getEyeLocation(), // Creates a raytrace to detect the HitBlock.
                player.getEyeLocation().getDirection(),
                100,
                FluidCollisionMode.NEVER);



        BukkitRunnable runnable = new BukkitRunnable() {
            int tick = 0;
            Vector projVelocity = projectile.getVelocity();
            @Override
            public void run() {

                if (tick == 60){ // Kills the projectile and the runnable after 3 seconds.
                    projectile.remove();
                    cancel();
                    return;
                }
                else {
                    tick++;
                }



                assert result != null;
                if (projectile.getLocation().equals(Objects.requireNonNull(result.getHitBlock()).getLocation())) { // Detects if the projectile has hit the raytraced block.

                    Vector newVelocity = result.getHitPosition().multiply(projVelocity.dot(result.getHitPosition())).multiply(-2);
                    projectile.setVelocity(newVelocity); // Reflects the projectile off the raytraced wall.

                }


                Collection<Entity> entities = player.getWorld().getNearbyEntities(projectile.getLocation(), 0.7, 0.7, 0.7);
                for (Entity e : entities) { // Damage all entities in that block space
                    if (e instanceof Damageable damageable && !e.equals(player))
                        damageable.damage(5, player);
                }

            }
        };
        runnable.runTaskTimer(schedulerPlugin, 0, 0);


        return true;
    }

}
