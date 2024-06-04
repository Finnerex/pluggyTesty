package co.tantleffbeef.pluggytesty.war;

public record BlockPosition(int x, int y, int z) {
    public boolean isChunkPosition() {
        return x % 16 == 0 && y == 0 && z % 16 == 0;
    }

    public BlockPosition getChunkPosition() {
        if (isChunkPosition())
            return this;
        else
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
