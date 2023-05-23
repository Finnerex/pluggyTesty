package co.tantleffbeef.pluggytesty.expeditions.loading;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;

public class ExpeditionInformation {
    public static class ExpeditionRoomInformation {
        private final RoomInformation roomInformation;
        private final Vector3i offset;

        public ExpeditionRoomInformation(@NotNull RoomInformation roomInformation, @NotNull Vector3i offset) {
            this.roomInformation = roomInformation;
            this.offset = offset;
        }
    }
}
