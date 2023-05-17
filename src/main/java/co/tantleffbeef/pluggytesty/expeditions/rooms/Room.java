package co.tantleffbeef.pluggytesty.expeditions.rooms;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Room {
    default void onFirstPlayerEnterRoom(@NotNull Player player) {}
    default void onPlayerEnterRoom(@NotNull Player player) {}
    default void onPlayerExitRoom(@NotNull Player player) {}
    default void onLastPlayerExitRoom(@NotNull Player player) {}
}
