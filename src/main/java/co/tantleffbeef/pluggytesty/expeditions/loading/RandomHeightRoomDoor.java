package co.tantleffbeef.pluggytesty.expeditions.loading;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class RandomHeightRoomDoor implements RoomDoor {
    public RandomHeightRoomDoor(@NotNull BlockFace direction,
                                @NotNull Material replacementMaterial,
                                @NotNull List<Integer> heightOffsets) {
        this.direction = direction;
        this.replacementMaterial = replacementMaterial;
        this.heightOffsets = heightOffsets;
    }

    private final BlockFace direction;
    private final Material replacementMaterial;
    private final List<Integer> heightOffsets;

    @Override
    public @NotNull BlockFace getDirection() {
        return direction;
    }

    @Override
    public @NotNull Material getReplacementMaterial() {
        return replacementMaterial;
    }

    @Override
    public int getHeightOffset(int seed) {
        final var random = new Random(seed);

        return heightOffsets.get(random.nextInt(heightOffsets.size()));
    }

    @Override
    public String toString() {
        return "RandomHeightRoomDoor{" +
                "direction=" + direction +
                ", replacementMaterial=" + replacementMaterial +
                ", heightOffsets=" + heightOffsets +
                '}';
    }
}
