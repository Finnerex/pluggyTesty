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

import java.util.Random;

public class ZapinatorItemType extends SimpleItemType implements InteractableItemType {

    private final Plugin schedulerPlugin;

    public ZapinatorItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.GOLDEN_HOE);
        this.schedulerPlugin = namespace;
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {
        if (player.hasCooldown(Material.GOLDEN_HOE))
            return false;

        final int attackNum = new Random().nextInt(2);


        final Location location = player.getEyeLocation();

        switch (attackNum) {
            case 0 -> attack1(location, player);
            case 1 -> attack2(location, player);
        }

        return false;
    }

    private Entity shootBolt(float range, Location location) {

        location.add(location.getDirection().multiply(1));
        final World world = location.getWorld();

        Entity entity = null;
        RayTraceResult result = world.rayTraceEntities(location, location.getDirection(), range * 10);

        if (result != null)
            entity = result.getHitEntity();

        if (!(entity instanceof Damageable))
            entity = null;

        for(float i = 0.1f; i < range; i += 0.1f) {
            location.add(location.getDirection().multiply(i));
            world.spawnParticle(Particle.FALLING_DRIPSTONE_LAVA, location, 4);
        }

        return entity;
    }

    /*
    * attacks:
    * slow high damage
    * peirce
    * high speed
    * wide spreading
    */

    private void attack1(Location l, Player player) {
        BukkitRunnable runnable = new BukkitRunnable() {
            int runs = 0;
            @Override
            public void run() {
                if (runs > 20) {
                    cancel();
                    return;
                }

//                l.add(l.getDirection().multiply(0.2));

                Entity hit = shootBolt(0.3f, l);
                if (hit instanceof Damageable damageable) {
                    damageable.damage(6, player);
                    cancel();
                }

                runs++;

            }
        };

        runnable.runTaskTimer(schedulerPlugin, 0, 1);

    }

    private void attack2(Location l, Player player) {
        BukkitRunnable runnable = new BukkitRunnable() {
            int runs = 0;
            @Override
            public void run() {
                if (runs > 20) {
                    cancel();
                    return;
                }

//                l.add(l.getDirection().multiply(0.2));

                Entity hit = shootBolt(1f, l);
                if (hit instanceof Damageable damageable) {
                    damageable.damage(2, player);
                    cancel();
                }
                runs++;

            }
        };

        runnable.runTaskTimer(schedulerPlugin, 0, 0);

    }

}
