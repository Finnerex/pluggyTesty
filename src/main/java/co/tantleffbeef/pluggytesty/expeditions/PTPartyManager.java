package co.tantleffbeef.pluggytesty.expeditions;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PTPartyManager implements PartyManager {
    private final List<Party> parties = new ArrayList<>();

    @Override
    public @Nullable Party getPartyWith(@NotNull Player player) {
        return parties.stream()
                .filter(party -> party.containsPlayer(player))
                .findAny()
                .orElse(null);
    }

    @Override
    public void registerParty(@NotNull Party party) {
        assert !parties.contains(party);
        parties.add(party);
    }

    @Override
    public void unregisterParty(@NotNull Party party) {
        assert parties.contains(party);
        parties.remove(party);
    }
}
