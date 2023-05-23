package co.tantleffbeef.pluggytesty.expeditions.loading;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

public class RoomInformation {
    public final RoomType roomType;

    /**
     * Tries to load the RoomInformation from a json object
     * @param json the object to load the room info from
     * @return a room information object representing the data
     *  stored in this json object
     */
    public static RoomInformation from(@NotNull JsonObject json) throws InvalidRoomInformationJsonException {
        // check if there is a type property
        if (!json.has("type"))
            throw new InvalidRoomInformationJsonException("No 'type' property found");

        final RoomType roomType;
        // Try to load the type property
        try {
            final var sRoomType = json.get("type").getAsString();
            roomType = RoomType.getRoomWithId(sRoomType).orElseThrow(() ->
                    new InvalidRoomInformationJsonException("No room type found with id '" + sRoomType + "'."));
        } catch (ClassCastException|IllegalStateException e) {
            throw new InvalidRoomInformationJsonException("Property 'type' is not a string.");
        }

        return new RoomInformation(roomType);
    }

    public RoomInformation(@NotNull RoomType roomType) {
        this.roomType = roomType;
    }
}
