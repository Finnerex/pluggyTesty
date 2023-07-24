package co.tantleffbeef.pluggytesty.expeditions.loading.typeadapters;

import co.tantleffbeef.pluggytesty.expeditions.loading.ExpeditionType;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ExpeditionTypeTypeAdapter implements JsonSerializer<ExpeditionType>, JsonDeserializer<ExpeditionType> {
    @Override
    public ExpeditionType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonPrimitive())
            throw new JsonParseException("not a primitive");

        return ExpeditionType.getExpeditionWithId(json.getAsString()).orElseThrow(() -> new JsonParseException("no expedition with this id"));
    }

    @Override
    public JsonElement serialize(ExpeditionType src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getId());
    }
}
