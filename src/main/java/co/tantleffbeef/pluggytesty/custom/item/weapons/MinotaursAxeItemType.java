package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

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

        final ItemDisplay axe = player.getWorld().spawn(player.getEyeLocation(), ItemDisplay.class, (display) -> display.setItemStack(new ItemStack(Material.GOLDEN_AXE)));

        BukkitRunnable runnable = new BukkitRunnable() {
            int distance = 0;
            @Override
            public void run() {
                if (distance > 20) {
                    axe.remove();
                    cancel();
                    return;
                }

                Location location = axe.getLocation();

                axe.teleport(location.add(location.getDirection()));
                axe.setRotation(location.getYaw(), location.getPitch() + 10);

                distance ++;
            }
        };

        runnable.runTaskTimer(schedulerPlugin, 0, 3);


        if (attacks >= 3)
            player.setCooldown(Material.GOLDEN_AXE, 40);

        return true;
    }
}
