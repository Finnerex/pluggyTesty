package co.tantleffbeef.pluggytesty.custom.item.utility;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;

public class DiggaItemType extends SimpleItemType implements InteractableItemType {

    public DiggaItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.PRISMARINE_SHARD);
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack item, Block b) {
        Block block = Dig(10000, player.getEyeLocation());

        if (block == null)
            return false;

        block.breakNaturally();
        return false;
    }

    private Block Dig(double range, Location location) {

        final World world = location.getWorld();

        RayTraceResult result = world.rayTraceBlocks(location, location.getDirection(), range, FluidCollisionMode.NEVER);

        if (result == null)
            return null;

        return result.getHitBlock();

    }


}
