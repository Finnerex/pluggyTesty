package co.tantleffbeef.pluggytesty.custom.item.utility;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class DashItemType extends SimpleItemType implements InteractableItemType {

    public DashItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.FEATHER);
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack item, Block block) {
        if (player.hasCooldown(Material.FEATHER))
            return false;

        player.getWorld().playSound(player, Sound.ENTITY_EGG_THROW, 1, 1);

        Vector direction = player.getEyeLocation().getDirection();

        player.setVelocity(direction.normalize().multiply(2).add(player.getVelocity()));

        player.setCooldown(Material.FEATHER, 120);

        return false;
    }

}
