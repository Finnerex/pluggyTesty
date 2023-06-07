package co.tantleffbeef.pluggytesty.expeditions.loading;

import co.tantleffbeef.pluggytesty.misc.InvalidJsonException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;
import org.joml.Vector3ic;

public class RoomInformationInstance {
    private final RoomInformation roomInformation;
    private final Vector3i offset;
    private final double rotation;

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

        return new RoomInformationInstance(room, offset, 0.0);
    }

    public RoomInformationInstance(@NotNull RoomInformation roomInformation,
                                   @NotNull Vector3i offset,
                                   double rotation) {
        this.roomInformation = roomInformation;
        this.offset = offset;
        this.rotation = rotation;
    }

    public @NotNull RoomInformation getRoomInformation() {
        return roomInformation;
    }

    public @NotNull Vector3ic getOffset() {
        return offset;
    }

    public double getRotation() {
        return rotation;
    }
}
