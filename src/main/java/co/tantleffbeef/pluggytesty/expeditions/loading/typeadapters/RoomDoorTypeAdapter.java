package co.tantleffbeef.pluggytesty.expeditions.loading.typeadapters;

import co.tantleffbeef.pluggytesty.expeditions.loading.ConsistentHeightRoomDoor;
import co.tantleffbeef.pluggytesty.expeditions.loading.RandomHeightRoomDoor;
import co.tantleffbeef.pluggytesty.expeditions.loading.RoomDoor;
import com.google.gson.*;

import java.lang.reflect.Type;

public class RoomDoorTypeAdapter implements JsonSerializer<RoomDoor>, JsonDeserializer<RoomDoor> {
    private final Gson gson = new Gson();

    @Override
    public JsonElement serialize(RoomDoor src, Type typeOfSrc, JsonSerializationContext context) {
        final var element = gson.toJsonTree(src);

        final var typeName = switch (src) {
            case ConsistentHeightRoomDoor ignored -> "consistent_height";
            case RandomHeightRoomDoor ignored -> "random_height";
            default -> throw new UnsupportedOperationException("room door type not supported");
        };

        if (!element.isJsonObject())
            throw new RuntimeException("idk mannn");

        final var object = element.getAsJsonObject();
        if (object.has("type"))
            throw new RuntimeException("not allowed!!!");

        object.addProperty("type", typeName);

        return object;
    }

    @Override
    public RoomDoor deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject())
            throw new JsonParseException("not json object");

        final var jsonObject = json.getAsJsonObject();
        if (!jsonObject.has("type"))
            throw new JsonParseException("no type element");

        final var typeString = jsonObject.get("type").getAsString();

        return switch (typeString) {
            case "consistent_height" -> gson.fromJson(json, ConsistentHeightRoomDoor.class);
            case "random_height" -> gson.fromJson(json, RandomHeightRoomDoor.class);
            default -> throw new JsonParseException("loading this room door type not supported");
        };
    }
}
