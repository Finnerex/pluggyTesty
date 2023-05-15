package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class MeowmereItemType extends SimpleItemType implements InteractableItemType {

    private final Plugin schedulerPlugin;

    public MeowmereItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.NETHERITE_SWORD);
        this.schedulerPlugin = namespace;
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {


        ItemDisplay projectile = player.getWorld().spawn(player.getEyeLocation(), ItemDisplay.class, (proj) -> { // Creates a Conduit projectile.
            proj.setItemStack(new ItemStack(Material.CONDUIT));
        });


        RayTraceResult result = player.getWorld().rayTraceBlocks(player.getEyeLocation(), // Creates a raytrace to detect the HitBlock.
                player.getEyeLocation().getDirection(),
                100,
                FluidCollisionMode.NEVER);



        Location playerLoc = player.getEyeLocation();

        BukkitRunnable runnable = new BukkitRunnable() {
            int tick = 0;
            boolean first = true;

            Vector vec = playerLoc.getDirection().clone().multiply(0.5);
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


                Location projLocation = projectile.getLocation();

                if (first)
                    projectile.teleport(projLocation.add(vec));
                else
                    projectile.teleport(projLocation.add(projLocation.getDirection().clone().multiply(0.5)));


                Location loc = new Location(player.getWorld(),result.getHitBlock().getLocation().getX() + 0.5, result.getHitBlock().getLocation().getY() + 0.5, result.getHitBlock().getLocation().getZ() + 0.5);

                if (projLocation.distance(loc) <= 0.5) { // Detects if the projectile has hit the raytraced block.
                    player.sendMessage("test");
                    if (result.getHitBlockFace() == BlockFace.UP || result.getHitBlockFace() == BlockFace.DOWN) {
                        player.sendMessage("test");
                        projLocation.getDirection().setY((projLocation.getDirection().getY()) * -1);
                    }

                    first = false;

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
