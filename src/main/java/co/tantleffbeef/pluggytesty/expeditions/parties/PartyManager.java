package co.tantleffbeef.pluggytesty.expeditions.parties;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PartyManager {
    /**
     * Gives the party that the player is in
     * @param player the player to check for
     * @return the party they are in or null if
     *  they are not in a party
     */
    @Nullable Party getPartyWith(@NotNull Player player);

    /**
     * Registers a party with the manager
     * Precondition: the manager does not contain the party
     * @param party the party to register
     */
    void registerParty(@NotNull Party party);

    /**
     * Unregisters a party with the manager
     * Precondition: the manager contains the party
     * @param party the party to remove
     */
    void unregisterParty(@NotNull Party party);
}
