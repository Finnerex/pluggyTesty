package co.tantleffbeef.pluggytesty.expeditions.loading.roomloading;

import co.tantleffbeef.pluggytesty.expeditions.loading.RoomInformationInstance;
import co.tantleffbeef.pluggytesty.expeditions.loading.RoomInformationCollection;
import co.tantleffbeef.pluggytesty.misc.InvalidJsonException;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface RoomLoader {
    @NotNull List<RoomInformationInstance> loadRooms(int seed);

    static RoomLoader from(@NotNull JsonObject json, @NotNull RoomInformationCollection collection)
            throws InvalidJsonException {
        if (!json.has("type"))
            throw new InvalidJsonException("Property 'type' does not exist.");

        final var typeElement = json.get("type");
        final String type;

        try {
            type = typeElement.getAsString();
        } catch (ClassCastException|IllegalStateException e) {
            throw new InvalidJsonException("Property 'type' is not a string.");
        }

        return switch (type) {
            case "random" -> RandomRoomLoader.from(json, collection);
            default -> throw new InvalidJsonException("Property 'type' does not exist");
        };
    }
}
