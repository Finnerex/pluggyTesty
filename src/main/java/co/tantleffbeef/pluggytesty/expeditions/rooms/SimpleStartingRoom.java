package co.tantleffbeef.pluggytesty.expeditions.rooms;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class SimpleStartingRoom implements StartingRoom {
    private final Location[] startingLocations;

    public SimpleStartingRoom(@NotNull Location[] startingLocations) {
        assert startingLocations.length > 0;
        this.startingLocations = startingLocations;
    }

    @Override
    public @NotNull Location[] getStartingLocations() {
        return startingLocations;
    }
}
