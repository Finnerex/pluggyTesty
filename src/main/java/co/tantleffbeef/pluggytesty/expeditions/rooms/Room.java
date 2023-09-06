package co.tantleffbeef.pluggytesty.expeditions.rooms;

import co.tantleffbeef.pluggytesty.expeditions.Expedition;
import co.tantleffbeef.pluggytesty.misc.Debug;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface Room {
    @NotNull Collection<Player> getPlayers();

    default void setExpedition(@NotNull Expedition expedition) {}

    default boolean hasPlayers() {
        return !getPlayers().isEmpty();
    }

    default void onFirstPlayerEnterRoom(@NotNull Player player) {
        Debug.log("Room.onFirstPlayerEnterRoom(): onFirstPlayerEnterRoom");
    }
    default void onPlayerEnterRoom(@NotNull Player player) { }
    default void onPlayerExitRoom(@NotNull Player player) { }
    default void onLastPlayerExitRoom(@NotNull Player player) { }
    default void onPlayerMove(@NotNull Player player, @NotNull Location location) { }
    default void onPlayerDamage(@NotNull Player player, @NotNull EntityDamageEvent event) { }
}
