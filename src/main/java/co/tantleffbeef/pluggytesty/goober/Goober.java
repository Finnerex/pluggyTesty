package co.tantleffbeef.pluggytesty.goober;

import co.tantleffbeef.pluggytesty.expeditions.parties.Party;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface Goober extends OfflineGoober {
    /**
     * Returns the player object this plugger wraps
     * @return the player object this plugger wraps
     */
    @NotNull Player asPlayer();

    /**
     * Tries to grab the plugger's party, if they
     * aren't in one returns Optional.empty()
     * @return an Optional hopefully holding the plugger's party
     */
    Optional<Party> getParty();

    /**
     * Grabs the party the plugger is in, or if they aren't in it
     * then it creates a new one with them as party leader
     * @return the party the plugger was already in or a newly created one
     */
    @NotNull Party getPartyOrCreate();
}
