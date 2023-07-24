package co.tantleffbeef.pluggytesty.expeditions.loading.roomloading;

import co.tantleffbeef.pluggytesty.expeditions.loading.RoomInformationInstance;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface RoomLoader {
    @NotNull Collection<RoomInformationInstance> loadRooms(int seed);
}
