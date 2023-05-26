package co.tantleffbeef.pluggytesty.expeditions.loading;

import co.tantleffbeef.pluggytesty.expeditions.rooms.SimpleStartingRoom;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public enum RoomType {
    SIMPLE_STARTING_ROOM("simple_starting_room", () -> new SimpleStartingRoom(new Location[]{
            new Location(Bukkit.getWorld("expeditions"), 0, 0, 0)
    }))

    ;

    private final String id;
    private final RoomConstructor constructor;

    RoomType(@NotNull String id, @NotNull RoomConstructor constructor) {
        this.id = id;
        this.constructor = constructor;
    }

    /**
     * Tries to grab the room with the id passed in. Has O(n) time complexity but
     * shouldn't run that often so who cares
     * @param searchId the id to look for
     * @return either the RoomType or Optional.empty()
     */
    public static Optional<RoomType> getRoomWithId(@NotNull String searchId) {
        // Loop through every room type and see if
        // any match the search id
        for (var type : values()) {
            // check if this is the type they're looking for
            if (type.id.equals(searchId))
                return Optional.of(type);
        }

        return Optional.empty();
    }

    public @NotNull RoomConstructor getConstructor() {
        return constructor;
    }
}
