package co.tantleffbeef.pluggytesty.expeditions;

import co.tantleffbeef.pluggytesty.expeditions.rooms.Room;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public interface Expedition {
    /**
     * Builds an expedition of this type at location
     * @param location where to build it
     * @param postBuildCallback a callback that runs once its finished
     */
    void build(@NotNull BukkitScheduler scheduler, @NotNull Location location, @NotNull Consumer<Expedition> postBuildCallback);

    /**
     * Starts the expedition with this party's players
     * @param party the party that will join the expedition
     */
    void start(@NotNull Party party);

    /**
     * Returns whether this expedition has been built
     * @return if this expedition is built
     */
    boolean isBuilt();

    /**
     * grabs the array of rooms in the expedition
     * @return an array of rooms and their metadata
     */
    @NotNull RoomMetadata[] getRooms();

    /**
     * Returns the party of players that is in this expedition
     *
     * @return the party of players in this expedition
     */
    Party getParty();

    /**
     * Grabs the room data for the room that the
     * player is in
     * this player must be in this expedition
     * @param player the player to grab the room for
     * @return the room player is in
     */
    @NotNull RoomMetadata getRoomWithPlayerData(@NotNull Player player);

    void setPlayerRoom(@NotNull RoomMetadata room, @NotNull Player player);

    /**
     * Ends an expedition
     */
    void end();
}
