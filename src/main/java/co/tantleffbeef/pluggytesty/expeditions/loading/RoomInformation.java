package co.tantleffbeef.pluggytesty.expeditions.loading;

import co.tantleffbeef.pluggytesty.misc.InvalidJsonException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;

public class RoomInformation {
    public final RoomType roomType;
    public final Path schematicPath;
    private final List<RoomDoor> doors;

    /**
     * Tries to load the RoomInformation from a json object
     * @param json the object to load the room info from
     * @return a room information object representing the data
     *  stored in this json object
     */
    public static RoomInformation from(@NotNull JsonObject json, @NotNull Path rootFolder) throws InvalidJsonException {
        // check if there is a type property
        if (!json.has("type"))
            throw new InvalidJsonException("No 'type' property found.");

        final RoomType roomType;
        // Try to load the type property
        try {
            final var sRoomType = json.get("type").getAsString();
            roomType = RoomType.getRoomWithId(sRoomType).orElseThrow(() ->
                    new InvalidJsonException("No room type found with id '" + sRoomType + "'."));
        } catch (ClassCastException|IllegalStateException e) {
            throw new InvalidJsonException("Property 'type' is not a string.");
        }

        // check if the property exists
        if (!json.has("path"))
            throw new InvalidJsonException("No 'path' property found.");

        final Path path;
        // try to load the property
        final JsonElement pathElement = json.get("path");
        if (!pathElement.isJsonPrimitive())
            throw new InvalidJsonException("Property 'path' is not a string.");

        final String pathString = pathElement.getAsString();
        path = rootFolder.resolve(pathString);


        // TODO: doors and heightOffset
        return new RoomInformation(roomType, path, null);
    }

    public RoomInformation(@NotNull RoomType roomType, @NotNull Path schematicPath, @Nullable List<RoomDoor> doors) {
        this.roomType = roomType;
        this.schematicPath = schematicPath;
        this.doors = doors;
    }

    public @Nullable List<RoomDoor> getDoors() {
        return doors;
    }
}
