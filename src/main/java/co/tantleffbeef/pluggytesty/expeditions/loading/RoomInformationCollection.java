package co.tantleffbeef.pluggytesty.expeditions.loading;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface RoomInformationCollection {
    Optional<RoomInformation> get(@NotNull String id);
}
