package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class YoyoItemType extends SimpleItemType implements InteractableItemType {

    private final Plugin schedulerPlugin;

    public YoyoItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.CHORUS_FRUIT);
        this.schedulerPlugin = namespace;
    }


    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {
        if (player.hasCooldown(Material.CHORUS_FRUIT))
            return true;

        ItemDisplay fruit = player.getWorld().spawn(player.getLocation(), ItemDisplay.class, (display) -> {
            display.setItemStack(itemStack);
        });



        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {

                fruit.teleport(player.getLocation().add(player.getLocation().getDirection().multiply(5));

                Collection<Entity> entities = player.getWorld().getNearbyEntities(fruit.getLocation(), 0.7, 0.7, 0.7);
                for (Entity e : entities) { // damage all entities in that block space
                    if (e instanceof Damageable damageable && !e.equals(player))
                        damageable.damage(5, player);
                }

            }
        };

        runnable.runTaskTimer(schedulerPlugin, 0, 0);



        return true;
    }

}
