package co.tantleffbeef.pluggytesty.expeditions.rooms;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface StartingRoom extends Room {
    /**
     * Adds the initial players to this room
     * @param players the players to add
     */
    void addInitialPlayers(@NotNull Collection<Player> players);

    /**
     * Spreads the players around the room and does other startup
     */
    void initialRoomStartup();
}
