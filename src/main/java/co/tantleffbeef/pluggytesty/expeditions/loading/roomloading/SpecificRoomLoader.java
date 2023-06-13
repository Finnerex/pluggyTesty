package co.tantleffbeef.pluggytesty.expeditions.loading.roomloading;

import co.tantleffbeef.pluggytesty.expeditions.loading.RoomInformationInstance;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class SpecificRoomLoader implements RoomLoader {
    private final Collection<RoomInformationInstance> rooms;

    public SpecificRoomLoader(@NotNull Collection<RoomInformationInstance> rooms) {
        this.rooms = rooms;
    }

    @Override
    public @NotNull Collection<RoomInformationInstance> loadRooms(int seed) {
        return rooms;
    }
}
