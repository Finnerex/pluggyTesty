package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


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

        // blue glass with lantern inside?
        ItemDisplay glass = w.spawn(location, ItemDisplay.class, (display) -> {
           display.setItemStack(new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS));

           w.spawn(location, ItemDisplay.class, inside -> {
              inside.setItemStack(new ItemStack(Material.SEA_LANTERN));
              display.addPassenger(inside);
              // make smaller
              Transformation transformation = inside.getTransformation();
              transformation.getScale().set(0.3);
              inside.setTransformation(transformation);
           });

        });

        BukkitRunnable runnable = new BukkitRunnable() {

            int runs = 0; // period 2 ticks

            @Override
            public void run() {

                if (runs > 150) {
                    glass.getPassengers().get(0).remove();
                    glass.remove();
                    cancel();
                    return;
                }

                glass.teleport(location.add(direction));

                runs ++;
            }
        };

        runnable.runTaskTimer(schedulerPlugin, 0, 2);

        return false;
    }
}