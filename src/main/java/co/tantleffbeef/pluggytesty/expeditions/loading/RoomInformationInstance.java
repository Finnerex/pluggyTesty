package co.tantleffbeef.pluggytesty.expeditions.loading;

import co.tantleffbeef.pluggytesty.misc.InvalidJsonException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

    public static RoomInformationInstance from(@NotNull JsonObject json,
                                               @NotNull RoomInformationCollection possibleRooms)
            throws InvalidJsonException {
        final RoomInformation room;
        final Vector3i offset;

        // Check if it contains the property
        if (!json.has("id"))
            throw new InvalidJsonException("Property 'id' does not exist.");

        // Grab the id element
        final JsonElement idElement = json.get("id");
        if (!idElement.isJsonPrimitive())
            throw new InvalidJsonException("Property 'id' is not a string.");

        final var idString = idElement.getAsString();

        // Grab the room if valid
        room = possibleRooms.get(idString).orElseThrow(() ->
                new InvalidJsonException("There is no room with id '" + idString + "'."));

        // Check if it has the property
        if (!json.has("offset"))
            throw new InvalidJsonException("Property 'offset' does not exist.");

        // Grab the offset element
        final var offsetElement = json.get("offset");

        // Check if its actually an array
        if (!offsetElement.isJsonArray())
            throw new InvalidJsonException("Property 'offset' is not an array.");

        final var offsetArray = offsetElement.getAsJsonArray();

        if (offsetArray.size() != 3)
            throw new InvalidJsonException("Array 'offset' has size other than 3.");

        // try and create a vector from it
        try {
            offset = new Vector3i(
                    offsetArray.get(0).getAsInt(),
                    offsetArray.get(1).getAsInt(),
                    offsetArray.get(2).getAsInt()
            );
        } catch (ClassCastException|IllegalStateException e) {
            throw new InvalidJsonException("Array 'offset' is not an array of integers.");
        }

        return new RoomInformationInstance(room, null, offset, 0);
    }

    public RoomInformationInstance(@NotNull RoomInformation roomInformation,
                                   @Nullable Collection<RoomDoor> doors,
                                   @NotNull Vector3i offset,
                                   int rotation) {
        this.roomInformation = roomInformation;
        this.doors = new ArrayList<>();
        if (doors != null) {
            doors.forEach(door -> this.doors.add(new RoomDoor(rotateFace(door.getDirection(), rotation), door.getReplacementMaterial(), door.getHeightOffset())));
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
