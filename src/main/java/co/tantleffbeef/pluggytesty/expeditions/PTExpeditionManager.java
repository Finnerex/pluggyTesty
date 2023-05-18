package co.tantleffbeef.pluggytesty.expeditions;

import co.tantleffbeef.pluggytesty.misc.Debug;
import co.tantleffbeef.pluggytesty.misc.ErrorCode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
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

    @Override
    public boolean inExpedition(@NotNull Player player) {
        return expeditionPlayers.contains(player);
    }

    public void onPlayerMoved(@NotNull Player player, @NotNull Location to) {
        assert expeditionPlayers.contains(player);

        // Grab the expedition they're in
        final var expedition = partyExpeditionMap.get(playerPartyMap.get(player.getUniqueId()));

        // Grab the room they're in
        final var roomData = expedition.getRoomWithPlayerData(player);

        // Turn their new position into a vector so we can see if its inside the room
        final var newPosition = to.toVector();
        final var oldRoom = roomData.room();
        // check if they're inside the room
        if (newPosition.isInAABB(roomData.minimum().toVector(), roomData.maximum().toVector())) {
            oldRoom.onPlayerMove(player, to);
            return;
        }

        // If not, figure out what room they're in now
        try {
            // Grab the new room
            final var newRoomData = findRoomWithPlayer(expedition, newPosition);
            final var newRoom = newRoomData.room();
            // Let the expedition know that it changed
            expedition.setPlayerRoom(newRoomData, player);
            // Let the rooms know that it changed
            oldRoom.onPlayerExitRoom(player);
            if (oldRoom.getPlayers().size() < 1)
                oldRoom.onLastPlayerExitRoom(player);

            if (newRoom.getPlayers().size() < 1)
                newRoom.onFirstPlayerEnterRoom(player);
            newRoom.onPlayerEnterRoom(player);
            newRoom.onPlayerMove(player, to);
        } catch (InvalidExpeditionStateException e) {
            expedition.end();
            Debug.alwaysError(ErrorCode.PLAYER_ESCAPED_EXPEDITION_ROOM +
                    "an expedition had to be forcibly ended because a player was not in a room");
            throw new RuntimeException(e);
        }
    }

    private RoomMetadata findRoomWithPlayer(@NotNull Expedition expedition, @NotNull Vector playerLocation)
            throws InvalidExpeditionStateException {
        // Grab all the rooms in the expedition
        final RoomMetadata[] rooms = expedition.getRooms();

        // Loop through every room and find out which one the player is in
        for (final RoomMetadata roomData : rooms) {
            if (!playerLocation.isInAABB(roomData.minimum().toVector(), roomData.maximum().toVector()))
                continue;

            return roomData;
        }

        // If we couldn't find one throw an exception
        throw new InvalidExpeditionStateException("unable to find the room that a player is in");
    }
}
