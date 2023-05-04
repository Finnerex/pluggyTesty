package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.Collection;


public class MinotaursAxeItemType extends SimpleItemType implements InteractableItemType {

    private int attacks = 0;
    private Plugin schedulerPlugin;

    public MinotaursAxeItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.GOLDEN_AXE);
        this.schedulerPlugin = namespace;
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack item, Block block) {
        if(player.hasCooldown(Material.GOLDEN_AXE))
            return true;

        attacks ++;

        Location l = player.getEyeLocation();

        final ItemDisplay axe = player.getWorld().spawn(l, ItemDisplay.class, (display) -> {
            display.setItemStack(new ItemStack(Material.GOLDEN_AXE));
            display.setRotation(l.getYaw() - 90, 0/*l.getPitch()*/);
        });

        final Vector direction = l.getDirection()/*.rotateAroundY(90)*/;

        BukkitRunnable runnable = new BukkitRunnable() {
            int distance = 0;
            final int attack = attacks;
            @Override
            public void run() {
                if (distance > 50) {
                    if (attacks == attack)
                        attacks = 0;
                    axe.remove();
                    cancel();
                    return;
                }

                Location location = axe.getLocation();

                final Transformation t = axe.getTransformation();
                t.getRightRotation().rotateLocalZ((float) Math.toRadians(10));/*rotateAxis((float) Math.toRadians(10), perpendicular);*/
                axe.setTransformation(t);

                Collection<Entity> entities = player.getWorld().getNearbyEntities(location, 1, 1, 1);
                for (Entity e : entities) { // damage all entities in that block space
                    if (e instanceof Damageable damageable && !e.equals(player))
                        damageable.damage(5, player);
                }

                axe.teleport(location.add(direction.clone().multiply(0.9)));

                distance ++;
            }
        };

        runnable.runTaskTimer(schedulerPlugin, 0, 0);


        if (attacks >= 3) {
            player.setCooldown(Material.GOLDEN_AXE, 40);
            attacks = 0;
        }


        return true;
    }
}
