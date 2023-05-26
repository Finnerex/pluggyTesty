package co.tantleffbeef.pluggytesty.expeditions.loading;

import co.tantleffbeef.pluggytesty.misc.InvalidJsonException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;

public class ExpeditionInformation {
    public final List<ExpeditionRoomInformation> roomInformationList;
    public final ExpeditionType expeditionType;

    public static class ExpeditionRoomInformation {
        public final RoomInformation roomInformation;
        public final Vector3i offset;

        public static ExpeditionRoomInformation from(@NotNull JsonObject json,
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

            return new ExpeditionRoomInformation(room, offset);
        }

        public ExpeditionRoomInformation(@NotNull RoomInformation roomInformation, @NotNull Vector3i offset) {
            this.roomInformation = roomInformation;
            this.offset = offset;
        }
    }

    public static ExpeditionInformation from(@NotNull JsonObject json,
                                             @NotNull RoomInformationCollection possibleRooms)
            throws InvalidJsonException {
        final List<ExpeditionRoomInformation> roomsList;
        final ExpeditionType type;

        // Check if it contains the property
        if (!json.has("rooms"))
            throw new InvalidJsonException("Property 'rooms' does not exist.");

        // Create a place to store it
        final JsonArray roomsArray;

        try {
            // Try and grab the property
             roomsArray = json.getAsJsonArray("rooms");
        } catch (ClassCastException e) {
            // If that is not what it is then give up
            throw new InvalidJsonException("Property 'rooms' is not an array.");
        }

        // Create a list to hold all of the rooms
        roomsList = new ArrayList<>();

        // Loop through all the elements of rooms
        for (int i = 0; i < roomsArray.size(); i++) {
            final JsonElement element = roomsArray.get(i);

            // Check if its actually a json object
            if (!element.isJsonObject())
                throw new InvalidJsonException("Element " + i + " of property 'rooms' is not an object.");

            final JsonObject roomObject = element.getAsJsonObject();

            roomsList.add(ExpeditionRoomInformation.from(roomObject, possibleRooms));
        }

        // Check if it contains the property
        if (!json.has("type"))
            throw new InvalidJsonException("Property 'type' does not exist.");

        final JsonElement typeElement = json.get("type");
        if (!typeElement.isJsonPrimitive())
            throw new InvalidJsonException("Property 'type' is not a string.");

        final var typeString = typeElement.getAsString();

        type = ExpeditionType.getExpeditionWithId(typeString).orElseThrow(() ->
                new InvalidJsonException("There is no expedition type with id '" + typeString + "'."));

        return new ExpeditionInformation(roomsList, type);
    }

    public ExpeditionInformation(@NotNull List<ExpeditionRoomInformation> rooms,
                                 @NotNull ExpeditionType type) {
        this.roomInformationList = rooms;
        this.expeditionType = type;
    }
}
