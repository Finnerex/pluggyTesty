package co.tantleffbeef.pluggytesty.expeditions.loading.typeadapters;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.nio.file.Path;

public class PathTypeAdapter implements JsonDeserializer<Path>, JsonSerializer<Path> {
    private final Path basePath;

    public PathTypeAdapter(@NotNull Path basePath) {
        this.basePath = basePath;
    }

    @Override
    public Path deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonPrimitive())
            throw new JsonParseException("not json primitive");

        return basePath.resolve(json.getAsString());
    }

    @Override
    public JsonElement serialize(Path src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(basePath.relativize(src.normalize()).normalize().toString());
    }
}
