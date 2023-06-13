package co.tantleffbeef.pluggytesty.goober;

import co.tantleffbeef.pluggytesty.expeditions.ExpeditionController;
import co.tantleffbeef.pluggytesty.expeditions.parties.PartyManager;
import co.tantleffbeef.pluggytesty.levels.LevelController;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class GooberStateController {
    private final LevelController levelController;
    private final PartyManager partyManager;
    private final ExpeditionController expeditionController;
    private final Server server;
    private final Map<UUID, PTGoober> gooberMap;

    public GooberStateController(@NotNull LevelController levelController,
                                 @NotNull PartyManager partyManager,
                                 @NotNull ExpeditionController expeditionController,
                                 @NotNull Server server) {
        this.levelController = levelController;
        this.partyManager = partyManager;
        this.expeditionController = expeditionController;
        this.server = server;
        this.gooberMap = new HashMap<>();
    }

    public OfflineGoober wrapOfflinePlayer(@NotNull OfflinePlayer player) {
        return Objects.requireNonNullElseGet(gooberMap.get(player.getUniqueId()),
                () -> createGoober(player));
    }

    public Goober wrapPlayer(@NotNull Player player) {
        return Objects.requireNonNullElseGet(gooberMap.get(player.getUniqueId()),
                () -> createGoober(player));
    }

    private PTGoober createGoober(@NotNull OfflinePlayer offlinePlayer) {
        assert !gooberMap.containsKey(offlinePlayer.getUniqueId());

        final var uuid = offlinePlayer.getUniqueId();

        final PTGoober goober;
        final var onlinePlayer = server.getPlayer(uuid);

        // if online
        if (onlinePlayer != null) {
            goober = new PTGoober(onlinePlayer, levelController, partyManager, expeditionController, server);
        } else {
            goober = new PTGoober(offlinePlayer, levelController, partyManager, expeditionController, server);
        }

        gooberMap.put(uuid, goober);

        return goober;
    }

    void onPlayerJoin(@NotNull Player whoJoined) {
        final var uuid = whoJoined.getUniqueId();

        if (!gooberMap.containsKey(uuid))
            return;

        final var goober = gooberMap.get(uuid);

        goober.setOnline(whoJoined);
    }

    void onPlayerLeave(@NotNull OfflinePlayer whoLeft) {
        final var uuid = whoLeft.getUniqueId();

        if (!gooberMap.containsKey(uuid))
            return;

        final var goober = gooberMap.get(uuid);

        goober.setOffline(whoLeft);
    }
}
