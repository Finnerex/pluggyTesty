package co.tantleffbeef.pluggytesty.expeditions.loading.typeadapters;

import co.tantleffbeef.pluggytesty.expeditions.loading.RoomInformation;
import com.google.common.collect.BiMap;
import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class RoomInformationTypeAdapter implements JsonSerializer<RoomInformation>, JsonDeserializer<RoomInformation> {
    private final BiMap<String, RoomInformation> roomInformationBiMap;

    public RoomInformationTypeAdapter(@NotNull BiMap<String, RoomInformation> roomInformationBiMap) {
        this.roomInformationBiMap = roomInformationBiMap;
    }

    @Override
    public RoomInformation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonPrimitive())
            throw new JsonParseException("not json primitive");

        final String key = json.getAsString();
        if (!roomInformationBiMap.containsKey(key))
            throw new JsonParseException("could not find corresponding room information");

        return roomInformationBiMap.get(key);
    }

    @Override
    public JsonElement serialize(RoomInformation src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(roomInformationBiMap.inverse().get(src));
    }
}
