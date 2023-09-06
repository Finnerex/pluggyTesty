package co.tantleffbeef.pluggytesty.expeditions;

import org.bukkit.Location;

public class RoomTransform {
    private final Location corner;
    private final int roomSize;
    private final int angle;

    public RoomTransform(Location corner, int roomSize, int angle) {
        this.corner = corner;
        this.roomSize = roomSize;

        while (angle < 0)
            angle = 360 - angle;

        if (angle >= 360)
            angle %= 360;

        this.angle = angle;
    }

    public Location getCorner() {
        return corner;
    }

    public Location getLocation(double x, double y, double z) {

        // 2 x, 1 z
        // x  x  x  x  x
        // x  x  x  x  x
        // x  x  x  x  x
        // x  x  o  x  x
        // x  x  x  x  x
        double newX;
        double newZ;

        if (angle == 90) {
            // x  x  x  x  x
            // x  x  x  x  x
            // x  x  x  x  x
            // x  x  o  x  x
            // x  x  x  x  x

            // x  x  x  x  x
            // x  x  x  x  x
            // x  o  x  x  x
            // x  x  x  x  x
            // x  x  x  x  x

            newX = z;
            newZ = x;
        } else if (angle == 180) {
            // x  x  x  x  x
            // x  x  x  x  x
            // x  x  x  x  x
            // x  o  x  x  x
            // x  x  x  x  x

            // x  x  x  x  x
            // x  x  x  o  x
            // x  x  x  x  x
            // x  x  x  x  x
            // x  x  x  x  x

            newX = roomSize - x;
            newZ = roomSize - z;
        } else if (angle == 270) {
            // x  x  x  x  x
            // x  x  x  x  x
            // x  x  x  x  x
            // x  o  x  x  x
            // x  x  x  x  x

            // x  x  x  x  x
            // x  x  x  x  x
            // x  x  x  x  x
            // x  x  x  o  x
            // x  x  x  x  x

            newX = roomSize - z;
            newZ = roomSize - x;
        } else {
            newX = x;
            newZ = z;
        }

        return corner.clone().add(newX, y, newZ);
    }
}
