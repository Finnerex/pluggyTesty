package co.tantleffbeef.pluggytesty.misc;

import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public final class BlockFaceMath {
    public static @NotNull BlockFace rotateFace(@NotNull BlockFace original, int theta) {
        final int originalDirection = calculateAngle(original);

        final int newDirection = (originalDirection + (360 + theta)) % 360;

        return findBlockFace(newDirection);
    }

    public static @NotNull BlockFace findBlockFace(int theta) {
        return switch (theta) {
            case 0 -> BlockFace.EAST;
            case 90 -> BlockFace.NORTH;
            case 180 -> BlockFace.WEST;
            case 270 -> BlockFace.SOUTH;
            default -> throw new IllegalStateException("idk what happened man");
        };
    }

    /**
     * Calculates the counter-clockwise angle from east (so east = 0) in degrees
     * but only for cardinal directions
     * @param direction the direction in question
     * @return read above
     */
    public static int calculateAngle(@NotNull BlockFace direction) {
        return switch (direction) {
            case EAST -> 0;
            case NORTH -> 90;
            case WEST -> 180;
            case SOUTH -> 270;
            default -> throw new IllegalArgumentException("direction not cardinal");
        };
    }
}
