package co.tantleffbeef.pluggytesty.plugger;

import co.tantleffbeef.pluggytesty.expeditions.PartyManager;
import co.tantleffbeef.pluggytesty.levels.LevelController;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PluggerFactory {
    private final LevelController levelController;
    private final PartyManager partyManager;
    private final Server server;

    public PluggerFactory(@NotNull LevelController levelController,
                          @NotNull PartyManager partyManager,
                          @NotNull Server server) {
        this.levelController = levelController;
        this.partyManager = partyManager;
        this.server = server;
    }

    public OfflinePlugger wrapOfflinePlayer(@NotNull OfflinePlayer player) {
        return new PTPlugger(player, levelController, partyManager, server);
    }

    public Plugger wrapPlayer(@NotNull Player player) {
        return new PTPlugger(player, levelController, partyManager, server);
    }
}
