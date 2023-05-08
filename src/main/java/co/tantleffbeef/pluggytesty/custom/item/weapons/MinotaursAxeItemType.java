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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class MinotaursAxeItemType extends SimpleItemType implements InteractableItemType {

    private final Plugin schedulerPlugin;
    private final Map<UUID, Integer> attacks;

    public MinotaursAxeItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.GOLDEN_AXE);
        this.attacks = new HashMap<>();
        this.schedulerPlugin = namespace;
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack item, Block block) {
        if(player.hasCooldown(Material.GOLDEN_AXE))
            return true;

        UUID uuid = player.getUniqueId();

        attacks.putIfAbsent(uuid, 0);

        attacks.put(uuid, attacks.get(uuid) + 1);

        Location l = player.getEyeLocation();

        final ItemDisplay axe = player.getWorld().spawn(l, ItemDisplay.class, (display) -> {
            display.setItemStack(new ItemStack(Material.GOLDEN_AXE));
            display.setRotation(l.getYaw() - 90, 0/*l.getPitch()*/);
        });

        final Vector direction = l.getDirection()/*.rotateAroundY(90)*/;

        BukkitRunnable runnable = new BukkitRunnable() {
            int distance = 0;

            @Override
            public void run() {
                if (distance > 30) {
                    axe.remove();
                    cancel();
                    return;
                }

                Location location = axe.getLocation();

                final Transformation t = axe.getTransformation();
                t.getRightRotation().rotateLocalZ((float) Math.toRadians(10));/*rotateAxis((float) Math.toRadians(10), perpendicular);*/
                axe.setTransformation(t);

                Collection<Entity> entities = player.getWorld().getNearbyEntities(location, 0.7, 0.7, 0.7);
                for (Entity e : entities) { // damage all entities in that block space
                    if (e instanceof Damageable damageable && !e.equals(player))
                        damageable.damage(5, player);
                }

                axe.teleport(location.add(direction.clone().multiply(0.9)));

                distance ++;
            }
        };

        runnable.runTaskTimer(schedulerPlugin, 0, 0);

        if (attacks.get(uuid) > 2) {
            player.setCooldown(Material.GOLDEN_AXE, 40);
            attacks.putIfAbsent(uuid, 0);
        }

        return true;
    }
}
