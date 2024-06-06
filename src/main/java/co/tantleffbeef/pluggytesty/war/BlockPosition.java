package co.tantleffbeef.pluggytesty.war;

import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record BlockPosition(int x, int y, int z) {
    @Contract("_ -> new")
    public static @NotNull BlockPosition of(Location location) {
        return new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public boolean isChunkPosition() {
        return x % 16 == 0 && y == 0 && z % 16 == 0;
    }

    public BlockPosition getChunkPosition() {
        if (isChunkPosition())
            return this;
        else
            return new BlockPosition(x - (x % 16), 0, z - (z % 16));
    }

    public static @NotNull BlockPosition chunkPositionOf(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        return new BlockPosition(x - (x % 16), 0, z - (z % 16));
    }

    public boolean isRegionPosition() {
        return x % 256 == 0 && y == 0 && z % 256 == 0;
    }

    public BlockPosition getRegionPosition() {
        if (isRegionPosition())
            return new BlockPosition(x - (x % 256), 0, z - (z % 256));
        else
            return this;
    }
}
