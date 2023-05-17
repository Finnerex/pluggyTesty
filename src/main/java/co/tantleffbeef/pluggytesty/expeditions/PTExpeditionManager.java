package co.tantleffbeef.pluggytesty.expeditions;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class PTExpeditionManager implements ExpeditionManager {
    private final PartyManager partyManager;
    private final Set<Player> expeditionPlayers;
    private final List<Expedition> expeditions;
    private final Map<Party, Expedition> partyExpeditionMap;
    private final Map<UUID, Party> playerPartyMap;

    public PTExpeditionManager(@NotNull PartyManager partyManager) {
        this.partyManager = partyManager;
        this.expeditionPlayers = new HashSet<>();
        this.expeditions = new ArrayList<>();
        this.partyExpeditionMap = new HashMap<>();
        this.playerPartyMap = new HashMap<>();
    }

    @Override
    public void startExpedition(@NotNull Expedition builtExpedition, @NotNull Party party) {
        assert builtExpedition.isBuilt();

        // Register the expedition
        registerExpedition(builtExpedition, party);

        // then start it
        builtExpedition.start(party);
    }

    @Override
    public void buildExpedition(@NotNull Expedition expedition, Consumer<Expedition> postBuildCallback) {
        // TODO
    }

    private void registerExpedition(@NotNull Expedition expedition, @NotNull Party party) {
        expeditions.add(expedition);

        // Add the party to the expedition map
        partyExpeditionMap.put(party, expedition);
        // Lock the party so no one can join
        // or leave it
        party.setLocked(true);

        // Loop through all players in the party
        for (final var player : party.getOnlinePlayers()) {
            assert expeditionPlayers.contains(player);

            // Add the player to the set of players that are
            // currently in an expedition
            expeditionPlayers.add(player);

            // Add them to a map of their party
            playerPartyMap.put(player.getUniqueId(), party);
        }
    }
}
