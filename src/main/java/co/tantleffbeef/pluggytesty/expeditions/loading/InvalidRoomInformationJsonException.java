package co.tantleffbeef.pluggytesty.expeditions.loading;

import org.jetbrains.annotations.NotNull;

public class InvalidRoomInformationJsonException extends Exception {
    public InvalidRoomInformationJsonException(@NotNull String reason) {
        super(reason);
    }
}
