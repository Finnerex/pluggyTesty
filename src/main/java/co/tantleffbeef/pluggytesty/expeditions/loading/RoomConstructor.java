package co.tantleffbeef.pluggytesty.expeditions.loading;

import co.tantleffbeef.pluggytesty.expeditions.rooms.Room;
import com.google.gson.JsonObject;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface RoomConstructor {
    @NotNull Room construct(@NotNull Location minLocation, @NotNull JsonObject roomSettings);
}
