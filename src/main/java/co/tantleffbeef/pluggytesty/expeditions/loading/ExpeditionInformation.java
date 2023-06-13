package co.tantleffbeef.pluggytesty.expeditions.loading;

import co.tantleffbeef.pluggytesty.expeditions.loading.roomloading.RoomLoader;
import co.tantleffbeef.pluggytesty.expeditions.loading.roomloading.SpecificRoomLoader;
import co.tantleffbeef.pluggytesty.misc.InvalidJsonException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ExpeditionInformation {
    private final RoomLoader roomLoader;
    private final ExpeditionType expeditionType;

    public static ExpeditionInformation from(@NotNull JsonObject json,
                                             @NotNull RoomInformationCollection possibleRooms)
            throws InvalidJsonException {
        final List<RoomInformationInstance> roomsList;
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

            roomsList.add(RoomInformationInstance.from(roomObject, possibleRooms));
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

        return new ExpeditionInformation(new SpecificRoomLoader(roomsList), type);
    }

    public ExpeditionInformation(@NotNull RoomLoader roomLoader,
                                 @NotNull ExpeditionType type) {
        this.roomLoader = roomLoader;
        this.expeditionType = type;
    }



    public @NotNull ExpeditionType getExpeditionType() {
        return expeditionType;
    }

    public @NotNull RoomLoader getRoomLoader() {
        return roomLoader;
    }
}
