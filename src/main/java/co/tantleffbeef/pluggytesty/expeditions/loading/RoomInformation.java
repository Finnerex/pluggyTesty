package co.tantleffbeef.pluggytesty.expeditions.loading;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class RoomInformation {
    public final RoomType roomType;
    public final Path schematicPath;
    public final JsonObject roomSettings;
    private final List<RoomDoor> doors;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomInformation that = (RoomInformation) o;
        return roomType == that.roomType && schematicPath.equals(that.schematicPath) && Objects.equals(doors, that.doors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomType, schematicPath, doors);
    }

    public RoomInformation(@NotNull RoomType roomType, @NotNull Path schematicPath, @Nullable JsonObject roomSettings, @Nullable List<RoomDoor> doors) {
        this.roomType = roomType;
        this.schematicPath = schematicPath;
        this.roomSettings = roomSettings;
        this.doors = doors;
    }

    @Override
    public String toString() {
        return "RoomInformation{" +
                "roomType=" + roomType +
                ", schematicPath=" + schematicPath +
                ", doors=" + doors +
                '}';
    }

    public @Nullable List<RoomDoor> getDoors() {
        return doors;
    }
}
