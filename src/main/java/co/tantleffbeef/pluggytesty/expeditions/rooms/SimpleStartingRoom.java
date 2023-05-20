package co.tantleffbeef.pluggytesty.expeditions.rooms;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleStartingRoom implements StartingRoom {
    private final Location[] startingLocations;
    private final List<Player> players;

    public SimpleStartingRoom(@NotNull Location[] startingLocations) {
        assert startingLocations.length > 0;
        this.startingLocations = startingLocations;
        this.players = new ArrayList<>();
    }

    @Override
    public @NotNull Location[] getStartingLocations() {
        return startingLocations;
    }

    @Override
    public @NotNull Collection<Player> getPlayers() {
        return players;
    }
}
