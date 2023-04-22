package co.tantleffbeef.pluggytesty.expeditions;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("unused")
public class Party {
    private final Server server;
    private final Set<UUID> playerSet;

    public Party(@NotNull Server server) {
        playerSet = new HashSet<>();
        this.server = server;
    }

    /**
     * Returns all players in the party, online or not
     * @return an immutable collection of all players
     */
    public Collection<OfflinePlayer> getAllPlayers() {
        return playerSet.stream()
                .map(server::getOfflinePlayer)
                .toList();
    }

    /**
     * Returns the ids of all players in the party, online or not
     * @return an immutable collection of all player ids
     */
    public Collection<UUID> getAllPlayerIds() {
        return playerSet.stream().toList();
    }

    /**
     * Gives all online players back
     * @return an immutable collection of all online players
     */
    public Collection<Player> getOnlinePlayers() {
        return playerSet.stream()
                .filter(uuid -> server.getPlayer(uuid) != null)
                .map(server::getPlayer)
                .toList();
    }

    /**
     * Returns all offline players
     * @return an immutable collection of all offline players
     */
    public Collection<OfflinePlayer> getOfflinePlayers() {
        return playerSet.stream()
                .filter(uuid -> server.getPlayer(uuid) == null)
                .map(server::getOfflinePlayer)
                .toList();
    }

    /**
     * Adds a player to the party
     * Precondition: player is not in the party
     * @param player the player to add
     */
    public void addPlayer(Player player) {
        assert !playerSet.contains(player.getUniqueId());
        playerSet.add(player.getUniqueId());
    }

    /**
     * Removes a player from the party
     * Precondition: player is in the party
     * @param player the player to remove
     */
    public void removePlayer(OfflinePlayer player) {
        assert playerSet.contains(player.getUniqueId());
        playerSet.remove(player.getUniqueId());
    }
}
