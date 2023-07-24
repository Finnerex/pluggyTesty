package co.tantleffbeef.pluggytesty.expeditions.loading.typeadapters;

import co.tantleffbeef.pluggytesty.expeditions.loading.RoomType;
import com.google.gson.*;

import java.lang.reflect.Type;

public class RoomTypeTypeAdapter implements JsonSerializer<RoomType>, JsonDeserializer<RoomType> {
    @Override
    public RoomType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonPrimitive())
            throw new JsonParseException("not a primitive");

        return RoomType.getRoomWithId(json.getAsString()).orElseThrow(() -> new JsonParseException("no room found with this id"));
    }

    @Override
    public JsonElement serialize(RoomType src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getId());
    }
}
