package co.tantleffbeef.pluggytesty.expeditions.rooms;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class SimpleStartingRoom implements StartingRoom {
    private final Location[] startingLocations;
    private final List<Player> players;

    public SimpleStartingRoom(@NotNull Location[] startingLocations) {
        assert startingLocations.length > 0;
        this.startingLocations = startingLocations;
        this.players = new ArrayList<>();
    }

    private void spreadPlayers() {
        assert startingLocations.length > 0;

        // Holds all potential locations a player could be sent to
        final List<Location> remainingLocations = new ArrayList<>();

        for (final var player : players) {
            // If we've run out of locations
            // then add them all back
            if (remainingLocations.size() < 1)
                remainingLocations.addAll(List.of(startingLocations));

            // Pick a random location
            final var locationIndex = new Random().nextInt(remainingLocations.size());

            // Send the player there
            player.teleport(remainingLocations.get(locationIndex));

            // Remove it from the list of locations
            remainingLocations.remove(locationIndex);
        }
    }

    @Override
    public void addInitialPlayers(@NotNull Collection<Player> players) {
        this.players.addAll(players);
    }

    @Override
    public void initialRoomStartup() {
        spreadPlayers();
    }

    @Override
    public @NotNull Collection<Player> getPlayers() {
        return players;
    }
}
