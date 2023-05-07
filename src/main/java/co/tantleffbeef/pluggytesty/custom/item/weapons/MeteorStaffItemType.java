package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.*;
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
import java.util.Random;

public class MeteorStaffItemType extends SimpleItemType implements InteractableItemType {

    private final Plugin schedulerPlugin;
    private final Material[] matList = {Material.DRIPSTONE_BLOCK, Material.BLACKSTONE, Material.COBBLED_DEEPSLATE, Material.GILDED_BLACKSTONE, Material.COBBLESTONE};

    public MeteorStaffItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.STONE_SHOVEL);
        this.schedulerPlugin = namespace;
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {
        if (player.hasCooldown(Material.STONE_SHOVEL))
            return true;

        final World w = player.getWorld();

        Location hitLocation = blockEntityCast(player.getEyeLocation(), player);

        Random r = new Random();
        final float x = r.nextFloat(-3, 3);
        final float z = r.nextFloat(-3, 3);


        BlockDisplay fallBlock = w.spawn(hitLocation.clone().add(new Vector(x, 20, z)), BlockDisplay.class, (display) -> {
            display.setBlock(matList[r.nextInt(matList.length)].createBlockData());
        });

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                Collection<Entity> entities = fallBlock.getNearbyEntities(0.5, 0.5, 0.5);
                entities.remove(fallBlock);

                if (fallBlock.getLocation().getY() <= hitLocation.getY() || !entities.isEmpty()) {
                    if (!entities.isEmpty()) {
                        for (Entity e : entities) {
                            if (e instanceof Damageable d)
                                d.damage(3, player);
                        }
                    }

                    w.playSound(hitLocation, Sound.ITEM_FIRECHARGE_USE, 1, 0.1f);
                    w.spawnParticle(Particle.END_ROD, hitLocation, 4);

                    fallBlock.remove();

                    cancel();
                    return;
                }



                Location location = fallBlock.getLocation();
                w.spawnParticle(Particle.FLAME, location, 1);

                fallBlock.teleport(location.add(location.clone().subtract(hitLocation).toVector().normalize().multiply(-2)));

            }
        };

        runnable.runTaskTimer(schedulerPlugin, 0, 0);

        player.setCooldown(Material.STONE_SHOVEL, 5);

        return true;
    }

    private Location blockEntityCast(Location location, Player player) {
        RayTraceResult result = location.getWorld().rayTraceEntities(location.add(location.getDirection()), location.getDirection(),40);

        if (result != null) {
            Entity entity = result.getHitEntity();
            if (entity != null && !entity.equals(player))
                return entity.getLocation();
        }

        result = location.getWorld().rayTraceBlocks(location, location.getDirection(),40);

        if (result != null) {
            if (result.getHitBlock() != null)
                return result.getHitBlock().getLocation();
        }

        return location.add(location.getDirection().multiply(10));

    }
}
