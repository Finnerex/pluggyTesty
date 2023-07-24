package co.tantleffbeef.pluggytesty.expeditions.loading;

import co.tantleffbeef.pluggytesty.expeditions.loading.roomloading.RoomLoader;
import org.jetbrains.annotations.NotNull;

public class ExpeditionInformation {
    private final RoomLoader roomLoader;
    private final ExpeditionType expeditionType;

    @Override
    public String toString() {
        return "ExpeditionInformation{" +
                "roomLoader=" + roomLoader +
                ", expeditionType=" + expeditionType +
                '}';
    }

    public ExpeditionInformation(@NotNull RoomLoader roomLoader,
                                 @NotNull ExpeditionType type) {
        this.roomLoader = roomLoader;
        this.expeditionType = type;
    }

    public @NotNull ExpeditionType getExpeditionType() {
        return expeditionType;
    }

    public @NotNull RoomLoader getRoomLoader() {
        return roomLoader;
    }
}
