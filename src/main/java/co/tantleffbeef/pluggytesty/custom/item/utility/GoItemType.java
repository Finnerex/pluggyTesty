package co.tantleffbeef.pluggytesty.custom.item.utility;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class GoItemType extends SimpleItemType implements InteractableItemType {

    public GoItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.LEATHER);
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack item, Block block) {
        // Go!!!
        Vector direction = player.getEyeLocation().getDirection().normalize();
        player.setVelocity(direction.multiply(2).add(player.getVelocity()));

        player.setCooldown(Material.LEATHER, 0);

        return false;
    }
}
