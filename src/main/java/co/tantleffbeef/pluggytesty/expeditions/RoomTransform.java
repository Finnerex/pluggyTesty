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
            // x  x  x  x  x
            // x  o  x  x  x    (1, 0)

            // x  x  x  x  x
            // o  x  x  x  x
            // x  x  x  x  x
            // x  x  x  x  x     z  rs-1 -  x
            // x  x  x  x  x    (0, 5 - 1 - 1)

            newX = z;
            newZ = roomSize - 1 - x;
        } else if (angle == 180) {
            // x  x  x  x  x
            // x  x  x  x  x
            // x  x  x  x  x
            // x  x  x  x  x
            // x  o  x  x  x

            // x  x  x  o  x
            // x  x  x  x  x
            // x  x  x  x  x
            // x  x  x  x  x
            // x  x  x  x  x

            newX = roomSize - 1 - x;
            newZ = roomSize - 1 - z;
        } else if (angle == 270) {
            // x  x  x  x  x
            // x  x  x  x  x
            // x  x  x  x  x
            // x  x  x  x  x
            // x  o  x  x  x   (1, 0)

            // x  x  x  x  x
            // x  x  x  x  x
            // x  x  x  x  x
            // x  x  x  x  o
            // x  x  x  x  x   (5 - 1 - 0, 1)

            newX = roomSize - 1 - z;
            newZ = x;
        } else {
            newX = x;
            newZ = z;
        }

        return corner.clone().add(newX, y, newZ);
    }
}
