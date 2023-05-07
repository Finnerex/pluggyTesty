package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class MeteorStaffItemType extends SimpleItemType implements InteractableItemType {

    public MeteorStaffItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.STONE_SHOVEL);
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {
        if (player.hasCooldown(Material.STONE_SHOVEL))
            return true;

        final World w = player.getWorld();

        Location hitLocation = blockEntityCast(player.getEyeLocation());

        Random r = new Random();
        final float x = r.nextFloat(-1, 1);
        final float z = r.nextFloat(-1, 1);

        FallingBlock fallBlock = w.spawnFallingBlock(hitLocation.add(new Vector(x, 10, z)), Material.DRIPSTONE_BLOCK.createBlockData());

        fallBlock.setVelocity(new Vector(-x / 2, -1, -z / 2));

        return true;
    }

    private Location blockEntityCast(Location location) {
        RayTraceResult result = location.getWorld().rayTrace(location, location.getDirection(), 30, FluidCollisionMode.SOURCE_ONLY, true, 1.2, null);

        if (result != null) {
            if (result.getHitEntity() != null)
                return result.getHitEntity().getLocation();
            if (result.getHitBlock() != null)
                return result.getHitBlock().getLocation();
        }

        return location.add(location.getDirection().multiply(10));

    }
}
