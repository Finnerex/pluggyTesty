package co.tantleffbeef.pluggytesty.expeditions.loading;

import co.tantleffbeef.pluggytesty.expeditions.rooms.Room;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface RoomConstructor {
    @NotNull Room construct(@NotNull Location minLocation);
}
