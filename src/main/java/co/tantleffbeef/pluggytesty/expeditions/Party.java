package co.tantleffbeef.pluggytesty.expeditions;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("unused")
public class Party {
    private final Server server;
    private final UUID owner;
    private final List<UUID> playerList;

    public Party(@NotNull Server server, @NotNull Player owner) {
        this.playerList = new ArrayList<>();
        this.server = server;
        this.owner = owner.getUniqueId();

        addPlayer(owner);
    }

    public @NotNull OfflinePlayer partyOwner() {
        return server.getOfflinePlayer(owner);
    }

    /**
     * Returns all players in the party, online or not
     * @return an immutable collection of all players
     */
    public Collection<OfflinePlayer> getAllPlayers() {
        return playerList.stream()
                .map(server::getOfflinePlayer)
                .toList();
    }

    /**
     * Returns the ids of all players in the party, online or not
     * @return an immutable collection of all player ids
     */
    public Collection<UUID> getAllPlayerIds() {
        return playerList.stream().toList();
    }

    /**
     * Gives all online players back
     * @return an immutable collection of all online players
     */
    public Collection<Player> getOnlinePlayers() {
        return playerList.stream()
                .filter(uuid -> server.getPlayer(uuid) != null)
                .map(server::getPlayer)
                .toList();
    }

    /**
     * Returns all offline players
     * @return an immutable collection of all offline players
     */
    public Collection<OfflinePlayer> getOfflinePlayers() {
        return playerList.stream()
                .filter(uuid -> server.getPlayer(uuid) == null)
                .map(server::getOfflinePlayer)
                .toList();
    }

    public boolean containsPlayer(OfflinePlayer player) {
        return playerList.contains(player.getUniqueId());
    }

    /**
     * Adds a player to the party
     * Precondition: player is not in the party
     * @param player the player to add
     */
    public void addPlayer(Player player) {
        assert !playerList.contains(player.getUniqueId());
        playerList.add(player.getUniqueId());
    }

    /**
     * Removes a player from the party
     * Precondition: player is in the party
     * @param player the player to remove
     */
    public void removePlayer(OfflinePlayer player) {
        assert playerList.contains(player.getUniqueId());
        playerList.remove(player.getUniqueId());
    }
}
