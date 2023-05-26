package co.tantleffbeef.pluggytesty.expeditions.rooms;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleExitRoom implements Room {
    private final List<Player> players = new ArrayList<>();
//    private final ExpeditionManager manager;

    /*public SimpleExitRoom(@NotNull ExpeditionManager manager) {
        this.manager = manager;
    }*/

    @Override
    public @NotNull Collection<Player> getPlayers() {
        return players;
    }

    /*@Override
    public void onPlayerEnterRoom(@NotNull Player player) {
        ((PTExpeditionManager) manager).quitExpedition(player);
    }*/
}
