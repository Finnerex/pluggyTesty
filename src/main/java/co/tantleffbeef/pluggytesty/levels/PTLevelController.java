package co.tantleffbeef.pluggytesty.levels;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PTLevelController implements LevelController {
    private final LevelStore store;

    public PTLevelController(@NotNull LevelStore store) {
        this.store = store;
    }

    @Override
    public int getPlayerLevel(OfflinePlayer player) {
        return store.retrieveLevel(player.getUniqueId());
    }

    @Override
    public void addLevels(OfflinePlayer player, int amount) {
        final var playerId = player.getUniqueId();

        // grab their current level
        final int currentLevel = store.retrieveLevel(playerId);
        // replace with their current level + 1
        store.storeLevel(playerId, currentLevel + amount);
    }

    @Override
    public void setLevel(OfflinePlayer player, int level) {
        store.storeLevel(player.getUniqueId(), level);
    }
}
