package co.tantleffbeef.pluggytesty.plugger;

import co.tantleffbeef.pluggytesty.expeditions.Party;
import co.tantleffbeef.pluggytesty.expeditions.PartyManager;
import co.tantleffbeef.pluggytesty.levels.LevelController;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PTPlugger implements Plugger {
    private final OfflinePlayer offlinePlayer;
    private final Player player;

    private final LevelController levelController;
    private final PartyManager partyManager;
    private final Server server;

    public PTPlugger(@NotNull OfflinePlayer player,
                      @NotNull LevelController levelController,
                      @NotNull PartyManager partyManager,
                      @NotNull Server server) {
        this.offlinePlayer = player;
        this.player = null;

        this.levelController = levelController;
        this.partyManager = partyManager;
        this.server = server;
    }

    public PTPlugger(@NotNull Player player,
                      @NotNull LevelController levelController,
                      @NotNull PartyManager partyManager,
                      @NotNull Server server) {
        this.offlinePlayer = player;
        this.player = player;

        this.levelController = levelController;
        this.partyManager = partyManager;
        this.server = server;
    }

    @Override
    public @NotNull OfflinePlayer asOfflinePlayer() {
        return offlinePlayer;
    }

    @Override
    public boolean isOnline() {
        return player != null;
    }

    @Override
    public int getLevel() {
        return levelController.getPlayerLevel(offlinePlayer);
    }

    @Override
    public void setLevel(int level) {
        levelController.setLevel(offlinePlayer, level);
    }

    @Override
    public void addLevels(int amount) {
        levelController.addLevels(offlinePlayer, amount);
    }

    @Override
    public @NotNull Player asPlayer() {
        // This should only be used
        // for an online Plugger
        assert player != null;

        return player;
    }

    @Override
    public Optional<Party> getParty() {
        // This should only be used
        // for an online Plugger
        assert player != null;

        // Grab the party if it exists
        return Optional.ofNullable(partyManager.getPartyWith(player));
    }

    @Override
    public @NotNull Party getPartyOrCreate() {
        // This should only be used
        // for an online Plugger
        assert player != null;

        // return the party that already exists
        // or make a new one
        return getParty().orElseGet(() -> {
            final var newParty = new Party(server, player);
            partyManager.registerParty(newParty);

            return newParty;
        });
    }
}
