package co.tantleffbeef.pluggytesty.expeditions.rooms;

import co.tantleffbeef.pluggytesty.expeditions.Expedition;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleExitRoom implements Room {
    private final List<Player> players = new ArrayList<>();
    private Expedition expedition;

    @Override
    public @NotNull Collection<Player> getPlayers() {
        return players;
    }

    @Override
    public void setExpedition(@NotNull Expedition expedition) {
        this.expedition = expedition;
    }

    @Override
    public void onPlayerEnterRoom(@NotNull Player player) {
        expedition.end();
    }
}
