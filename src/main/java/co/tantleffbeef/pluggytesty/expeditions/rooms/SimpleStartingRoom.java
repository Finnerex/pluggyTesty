package co.tantleffbeef.pluggytesty.expeditions.rooms;

import co.tantleffbeef.pluggytesty.expeditions.RoomTransform;
import com.google.gson.JsonObject;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleStartingRoom implements StartingRoom {
    private final Location spawnLocation;
    private final List<Player> players;

    public SimpleStartingRoom(@NotNull RoomTransform transform, @NotNull JsonObject roomSettings) {
        var spawnOffsets = roomSettings.getAsJsonArray("spawnOffset");

        spawnLocation = transform.getLocation(spawnOffsets.get(0).getAsFloat(),
                spawnOffsets.get(1).getAsFloat(),
                spawnOffsets.get(2).getAsFloat());
        this.players = new ArrayList<>();
    }

    private void spreadPlayers() {
        for (var player:
             players) {
            player.teleport(spawnLocation);
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
