package co.tantleffbeef.pluggytesty.expeditions.rooms;

import co.tantleffbeef.pluggytesty.expeditions.Expedition;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface Room {
    @NotNull Collection<Player> getPlayers();

    void setExpedition(@NotNull Expedition expedition);

    default boolean hasPlayers() {
        return !getPlayers().isEmpty();
    }

    default void onFirstPlayerEnterRoom(@NotNull Player player) {
        player.sendMessage("on first player enter room");
    }
    default void onPlayerEnterRoom(@NotNull Player player) {
        player.sendMessage("on player enter room");
    }
    default void onPlayerExitRoom(@NotNull Player player) {
        player.sendMessage("on player exit room");
    }
    default void onLastPlayerExitRoom(@NotNull Player player) {
        player.sendMessage("on last player exit room");
    }
    default void onPlayerMove(@NotNull Player player, @NotNull Location location) {
        player.sendMessage("on player move: " + location);
    }
}
