package co.tantleffbeef.pluggytesty.expeditions.rooms;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EmptyRoom implements Room {
    private final List<Player> players = new ArrayList<>();

    @Override
    public @NotNull Collection<Player> getPlayers() {
        return players;
    }
}
