package co.tantleffbeef.pluggytesty.expeditions.loading;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public class ConsistentHeightRoomDoor implements RoomDoor {
    private final BlockFace direction;
    private final Material replacementMaterial;
    private final int heightOffset;

    public ConsistentHeightRoomDoor(@NotNull BlockFace direction,
                                    @NotNull Material replacementMaterial,
                                    int heightOffset) {
        this.direction = direction;
        this.replacementMaterial = replacementMaterial;
        this.heightOffset = heightOffset;
    }

    public void fill(@NotNull Location minimumCorner,
                     @NotNull Location maximumCorner) {
        if (minimumCorner.getWorld() == null)
            throw new IllegalArgumentException("The corners must be in a world");
        if (!minimumCorner.getWorld().equals(maximumCorner.getWorld()))
            throw new IllegalArgumentException("Minimum and maximum corner are not in the same world");

        final var world = minimumCorner.getWorld();

        for (int ix = minimumCorner.getBlockX(); ix <= maximumCorner.getBlockX(); ix++) {
            for (int iy = minimumCorner.getBlockY(); iy <= maximumCorner.getBlockY(); iy++) {
                for (int iz = minimumCorner.getBlockZ(); iz <= maximumCorner.getBlockZ(); iz++) {
                    world.getBlockAt(ix, iy, iz).setType(getReplacementMaterial(), false);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "ConsistentHeightRoomDoor{" +
                "heightOffset=" + heightOffset +
                '}';
    }

    public @NotNull BlockFace getDirection() {
        return direction;
    }

    public @NotNull Material getReplacementMaterial() {
        return replacementMaterial;
    }

    public int getHeightOffset(int seed) {
        return heightOffset;
    }
}
