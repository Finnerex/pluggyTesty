package co.tantleffbeef.pluggytesty.expeditions.loading;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public interface RoomDoor {
    @NotNull BlockFace getDirection();

    @NotNull Material getReplacementMaterial();

    int getHeightOffset(int seed);
}
