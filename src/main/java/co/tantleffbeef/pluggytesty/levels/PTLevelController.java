package co.tantleffbeef.pluggytesty.levels;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import co.tantleffbeef.pluggytesty.goober.GooberStateController;
import co.tantleffbeef.pluggytesty.levels.event.GooberLevelChangeEvent;

public class PTLevelController implements LevelController {
    private final LevelStore store;
    private final PluginManager pluginManager;
    private final GooberStateController gStateController;

    public PTLevelController(@NotNull LevelStore store, @NotNull PluginManager pluginManager, @NotNull GooberStateController gStateController) {
        this.store = store;
        this.pluginManager = pluginManager;
        this.gStateController = gStateController;
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
        setLevel(player, currentLevel + amount);
    }

    @Override
    public void setLevel(OfflinePlayer player, int newLevel) {
        var wrapped = gStateController.wrapOfflinePlayer(player);
        // call the level change event
        var event = new GooberLevelChangeEvent(wrapped, wrapped.getLevel(), newLevel);
        pluginManager.callEvent(event);
        
        // if its cancelled then don't change the player's level
        if (event.isCancelled())
            return;
        
        store.storeLevel(player.getUniqueId(), newLevel);
    }
}
