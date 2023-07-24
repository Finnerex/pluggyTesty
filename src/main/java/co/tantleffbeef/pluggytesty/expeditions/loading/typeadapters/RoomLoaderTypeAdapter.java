package co.tantleffbeef.pluggytesty.expeditions.loading.typeadapters;

import co.tantleffbeef.pluggytesty.expeditions.loading.RoomInformation;
import co.tantleffbeef.pluggytesty.expeditions.loading.RoomType;
import co.tantleffbeef.pluggytesty.expeditions.loading.roomloading.RandomRoomLoader;
import co.tantleffbeef.pluggytesty.expeditions.loading.roomloading.RoomLoader;
import co.tantleffbeef.pluggytesty.expeditions.loading.roomloading.SpecificRoomLoader;
import com.google.common.collect.BiMap;
import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.nio.file.Path;

public class RoomLoaderTypeAdapter implements JsonDeserializer<RoomLoader>, JsonSerializer<RoomLoader> {
    private final Gson gson;

    public RoomLoaderTypeAdapter(@NotNull BiMap<String, RoomInformation> roomInformationBiMap, @NotNull Path basePath) {
        gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Path.class, new PathTypeAdapter(basePath))
                .registerTypeAdapter(RoomInformation.class, new RoomInformationTypeAdapter(roomInformationBiMap))
                .registerTypeAdapter(RoomType.class, new RoomTypeTypeAdapter())
                .create();
    }

    @Override
    public JsonElement serialize(RoomLoader src, Type typeOfSrc, JsonSerializationContext context) {
        final var element = gson.toJsonTree(src);

        final var typeName = switch (src) {
            case SpecificRoomLoader ignored -> "specific";
            case RandomRoomLoader ignored -> "random";
            default -> throw new UnsupportedOperationException("room loader not supported");
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
    public RoomLoader deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject())
            throw new JsonParseException("not json object");

        final var jsonObject = json.getAsJsonObject();
        if (!jsonObject.has("type"))
            throw new JsonParseException("no type element");

        final var typeString = jsonObject.get("type").getAsString();

        return switch (typeString) {
            case "specific" -> gson.fromJson(json, SpecificRoomLoader.class);
            case "random" -> gson.fromJson(json, RandomRoomLoader.class);
            default -> throw new JsonParseException("loading this room type not supported");
        };
    }
}
