package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import com.sun.source.tree.UsesTree;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

public class MeowmereItemType extends SimpleItemType implements InteractableItemType {

    private final Plugin schedulerPlugin;
    private final int COOLDOWN_TICKS = 10;

    public MeowmereItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.NETHERITE_SWORD);
        this.schedulerPlugin = namespace;
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);

        meta.setLore(Arrays.asList(ChatColor.DARK_GREEN + "Right-Click : Bouncing projectile", ChatColor.DARK_GREEN + "Cooldown : " + COOLDOWN_TICKS / 20f + "s"));

        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(),"dawg", 0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {
        if(player.hasCooldown(Material.NETHERITE_SWORD))
            return false;



        ItemDisplay projectile = player.getWorld().spawn(player.getEyeLocation(), ItemDisplay.class, (proj) -> { // Creates a Conduit projectile.
            proj.setItemStack(new ItemStack(Material.CONDUIT));
        });



        Location playerLoc = player.getEyeLocation();

        BukkitRunnable runnable = new BukkitRunnable() {
            int tick = 0;

            RayTraceResult result = player.getWorld().rayTraceBlocks(player.getEyeLocation(), // Creates a raytrace to detect the HitBlock.
                    player.getEyeLocation().getDirection(),
                    100,
                    FluidCollisionMode.NEVER);
            final Vector vel = playerLoc.getDirection().clone().multiply(0.5);
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

                projectile.teleport(projLocation.add(vel));


                if (result == null || result.getHitBlock() == null)
                    return;


                Location loc = new Location(player.getWorld(),result.getHitBlock().getLocation().getX() + 0.5, result.getHitBlock().getLocation().getY() + 0.5, result.getHitBlock().getLocation().getZ() + 0.5);

                if (projLocation.distance(loc) <= 1) { // Detects if the projectile has hit the raytraced block.

                    BlockFace face = result.getHitBlockFace();

                    if (face == null)
                        return;

                    switch (face) {
                        case UP, DOWN -> vel.setY((vel.getY()) * -1);
                        case WEST, EAST -> vel.setX((vel.getX()) * -1);
                        case NORTH, SOUTH -> vel.setZ((vel.getZ()) * -1);
                    }

                    result = player.getWorld().rayTraceBlocks(projLocation, vel, 100, FluidCollisionMode.NEVER);

                }



                Collection<Entity> entities = player.getWorld().getNearbyEntities(projectile.getLocation(), 0.7, 0.7, 0.7);
                for (Entity e : entities) { // Damage all entities in that block space
                    if (e instanceof Damageable damageable && !e.equals(player))
                        damageable.damage(5, player);
                }

            }
        };
        runnable.runTaskTimer(schedulerPlugin, 0, 0);

        player.setCooldown(Material.NETHERITE_SWORD, COOLDOWN_TICKS);

        return true;
    }

}
