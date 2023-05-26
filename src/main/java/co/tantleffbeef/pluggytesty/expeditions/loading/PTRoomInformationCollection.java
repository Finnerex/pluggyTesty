package co.tantleffbeef.pluggytesty.expeditions.loading;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PTRoomInformationCollection implements RoomInformationCollection {
    private final Map<String, RoomInformation> roomInformationMap;

    public PTRoomInformationCollection() {
        this.roomInformationMap = new HashMap<>();
    }

    @Override
    public Optional<RoomInformation> get(@NotNull String id) {
        return Optional.ofNullable(roomInformationMap.getOrDefault(id, null));
    }

    public void put(@NotNull String id, @NotNull RoomInformation info) {
        assert !roomInformationMap.containsKey(id);

        roomInformationMap.put(id, info);
    }
}
