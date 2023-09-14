package co.tantleffbeef.pluggytesty.expeditions.loading;

import co.tantleffbeef.pluggytesty.expeditions.RoomTransform;
import co.tantleffbeef.pluggytesty.expeditions.rooms.Room;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

public interface RoomConstructor {
    @NotNull Room construct(@NotNull RoomTransform transform, @NotNull JsonObject roomSettings);
}
