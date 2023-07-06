package co.tantleffbeef.pluggytesty.expeditions.loading;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import java.util.ArrayList;
import java.util.Collection;

import static co.tantleffbeef.pluggytesty.misc.BlockFaceMath.*;

public class RoomInformationInstance {
    private final RoomInformation roomInformation;
    private final Collection<RoomDoor> doors;
    private final Vector3i offset;
    private final int rotation;

    public RoomInformationInstance(@NotNull RoomInformation roomInformation,
                                   @Nullable Collection<RoomDoor> doors,
                                   @NotNull Vector3i offset,
                                   int rotation,
                                   int seed) {
        this.roomInformation = roomInformation;
        this.doors = new ArrayList<>();
        if (doors != null) {
            doors.forEach(door -> this.doors.add(new ConsistentHeightRoomDoor(rotateFace(door.getDirection(), rotation), door.getReplacementMaterial(), door.getHeightOffset(seed))));
        }
        this.offset = offset;
        this.rotation = rotation;
    }

    public @NotNull RoomInformation getRoomInformation() {
        return roomInformation;
    }

    public @NotNull Vector3ic getOffset() {
        return offset;
    }

    public int getRotation() {
        return rotation;
    }

    public @NotNull Collection<RoomDoor> getDoors() {
        return doors;
    }
}
