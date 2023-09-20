package co.tantleffbeef.pluggytesty.goober;

import co.tantleffbeef.pluggytesty.expeditions.Expedition;
import co.tantleffbeef.pluggytesty.expeditions.ExpeditionController;
import co.tantleffbeef.pluggytesty.expeditions.parties.Party;
import co.tantleffbeef.pluggytesty.expeditions.parties.PartyManager;
import co.tantleffbeef.pluggytesty.levels.LevelController;
import co.tantleffbeef.pluggytesty.nations.Nation;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class PTGoober implements Goober {
    private OfflinePlayer offlinePlayer;
    private Player player;
    private final UUID uuid;

    private final LevelController levelController;
    private final PartyManager partyManager;
    private final ExpeditionController expeditionController;
    private final Server server;

    public PTGoober(@NotNull OfflinePlayer player,
                    @NotNull LevelController levelController,
                    @NotNull PartyManager partyManager,
                    @NotNull ExpeditionController expeditionController,
                    @NotNull Server server) {
        this.offlinePlayer = player;
        this.player = null;
        this.uuid = player.getUniqueId();

        this.levelController = levelController;
        this.partyManager = partyManager;
        this.expeditionController = expeditionController;
        this.server = server;
    }

    public PTGoober(@NotNull Player player,
                    @NotNull LevelController levelController,
                    @NotNull PartyManager partyManager,
                    @NotNull ExpeditionController expeditionController,
                    @NotNull Server server) {
        this.offlinePlayer = player;
        this.player = player;
        this.uuid = player.getUniqueId();

        this.levelController = levelController;
        this.partyManager = partyManager;
        this.expeditionController = expeditionController;
        this.server = server;
    }

    @Override
    public @NotNull OfflinePlayer asOfflinePlayer() {
        return offlinePlayer;
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return uuid;
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
        if (player == null)
            throw new GooberOfflineException();

        return player;
    }

    @Override
    public Optional<Party> getParty() {
        // This should only be used
        // for an online Plugger
        if (player == null)
            throw new GooberOfflineException();

        // Grab the party if it exists
        return Optional.ofNullable(partyManager.getPartyWith(player));
    }

    @Override
    public @NotNull Party getPartyOrCreate() {
        // This should only be used
        // for an online Plugger
        if (player == null)
            throw new GooberOfflineException();

        // return the party that already exists
        // or make a new one
        return getParty().orElseGet(() -> {
            final var newParty = new Party(server, player);
            partyManager.registerParty(newParty);

            return newParty;
        });
    }

    @Override
    public Optional<Expedition> getExpedition() {
        // This should only be used
        // for an online Plugger
        if (player == null)
            throw new GooberOfflineException();

        if (!expeditionController.inExpedition(player))
            return Optional.empty();

        return Optional.of(expeditionController.getExpedition(player));
    }

    @Override
    public Optional<Nation> getNation() {
        // TODO: this
        return Optional.empty();
    }

    void setOnline(@NotNull Player toSet) {
        assert uuid.equals(toSet.getUniqueId());

        offlinePlayer = toSet;
        player = toSet;
    }

    void setOffline(@NotNull OfflinePlayer toSet) {
        assert uuid.equals(toSet.getUniqueId());

        offlinePlayer = toSet;
        player = null;
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof PTGoober g))
            return false;

        return uuid.equals(g.uuid);
    }
}
