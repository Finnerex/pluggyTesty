package co.tantleffbeef.pluggytesty.expeditions;

import co.tantleffbeef.pluggytesty.expeditions.parties.Party;
import co.tantleffbeef.pluggytesty.misc.Debug;
import co.tantleffbeef.pluggytesty.misc.ErrorCode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PTExpeditionController implements ExpeditionController {
//    private final RoomInformationCollection rooms;
//    private final LocationTraverser locationTraverser;
//    private final World world;
    private final Set<Player> expeditionPlayers;
    private final List<Expedition> expeditions;
    private final Map<Party, Expedition> partyExpeditionMap;
    private final Map<UUID, Party> playerPartyMap;
//    private final int maxExpeditionSize;

    public PTExpeditionController() {
        this.expeditionPlayers = new HashSet<>();
        this.expeditions = new ArrayList<>();
        this.partyExpeditionMap = new HashMap<>();
        this.playerPartyMap = new HashMap<>();
//        this.world = setupExpeditionWorld(server, expeditionWorldName);
//        this.locationTraverser = new LocationTraverser();
//        this.maxExpeditionSize = maxExpeditionSize;

//        rooms = new PTRoomInformationCollection();
    }

    @Override
    public void startExpedition(@NotNull Expedition builtExpedition, @NotNull Party party) {
        // Register the expedition
        registerExpedition(builtExpedition, party);
    }

    /*@Override
    public @NotNull CompletableFuture<Expedition> buildExpedition(@NotNull ExpeditionInformation buildInfo) {
        // Grab a location
        final var assignedLocation = locationTraverser.assignNextAvailableLocation();
        Debug.info("assignedLocation: " + assignedLocation);
        // Find the block the corner of the expedition should be at
        final var expeditionCorner = new Vector2i(assignedLocation).mul(maxExpeditionSize);
        Debug.info("expeditionCorner: " + assignedLocation);

        // Grab the world we need to paste in
        final var weWorld = BukkitAdapter.adapt(world);

        return CompletableFuture.supplyAsync(() -> {
            // grab the rooms
            final var roomInfoList = buildInfo.roomInformationList;
            // make a vector to mark the minimum position
            // so we can adjust the whole room over
            final var minimumPos = new Vector3i(roomInfoList.get(0).getOffset());

            // Loop through every room to find the minimum position
            for (var room : roomInfoList) {
//                final var info = room.roomInformation;
                final var offset = room.getOffset();

                if (offset.x() < minimumPos.x)
                    minimumPos.x = offset.x();
                if (offset.y() < minimumPos.y)
                    minimumPos.y = offset.y();
                if (offset.z() < minimumPos.z)
                    minimumPos.z = offset.z();
            }

            Debug.log("minimum pos: " + minimumPos);

            // This offset is used so that the expedition's minimum position is put at the position
            // its given to build at
            final var expeditionLocationWithOffset = minimumPos.mul(-1, new Vector3i()).add(expeditionCorner.x, 0, expeditionCorner.y);

            Debug.log("expeditionLocationWithOffset: " + expeditionLocationWithOffset);

            // Create something to hold the room metadata for all the rooms
            final var roomMetadataList = new ArrayList<RoomMetadata>();

            // Loop through every room and paste it
            for (var room : roomInfoList) {
                final var info = room.getRoomInformation();
                final var roomOffset = room.getOffset();
                final var schemPath = info.schematicPath;

                Debug.log("pasteLocation: " + expeditionLocationWithOffset.add(roomOffset, new Vector3i()));

                // figure out where to paste the room
                final var pasteLocation = BlockVector3Imp.at(
                        expeditionLocationWithOffset.x + roomOffset.x(),
                        expeditionLocationWithOffset.y + roomOffset.y(),
                        expeditionLocationWithOffset.z + roomOffset.z()
                );

                // This will store the location and stuff for this room
                final RoomMetadata roomData;

                try (final var schem = FaweAPI.load(schemPath.toFile())) {
                    // Set the origin to the minimum point so that it actually
                    // pastes it where you're trying to paste it
                    schem.setOrigin(schem.getMinimumPoint());

                    // Paste the room's schematic
                    final var pasteSession = schem.paste(weWorld, pasteLocation);
                    pasteSession.close();

                    final var maximumPoint = pasteLocation.add(schem.getDimensions());

                    Debug.log("minimumPoint: " + pasteLocation);
                    Debug.log("maximumPoint: " + maximumPoint);

                    // store the room's data
                    final var roomObject =
                            info.roomType.getConstructor().construct(bukkitLocationFromWE(world, pasteLocation));

                    roomData = new RoomMetadata(
                            roomObject,
                            // Give the room a bounding box based solely on its schematic
                            new RoomBoundingBox(
                                    bukkitLocationFromWE(world, pasteLocation),
                                    bukkitLocationFromWE(world, maximumPoint)
                            )
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // add this room to the list of rooms
                roomMetadataList.add(roomData);
            }

            final var expeditionLocationAsBukkitLocation = new Location(
                    world,
                    expeditionCorner.x,
                    0,
                    expeditionCorner.y
            );

            Debug.log("expeditionLocationAsBukkitLocation: " + expeditionLocationAsBukkitLocation);

            return buildInfo.expeditionType.getConstructor().construct(
                    this,
                    expeditionLocationAsBukkitLocation,
                    roomMetadataList.toArray(new RoomMetadata[0])
            );
        });
    }*/

    /*private @NotNull Location bukkitLocationFromWE(@NotNull World world, @NotNull BlockVector3 pos) {
        return new Location(world, pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
    }*/

    private void registerExpedition(@NotNull Expedition expedition, @NotNull Party party) {
        expeditions.add(expedition);

        // Add the party to the expedition map
        partyExpeditionMap.put(party, expedition);
        // Lock the party so no one can join
        // or leave it
        party.setLocked(true);

        // Loop through all players in the party
        for (final var player : party.getOnlinePlayers()) {
            assert !expeditionPlayers.contains(player);

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

    @Override
    public void endExpedition(@NotNull Expedition toEnd) {
        final var party = toEnd.getParty();

        for (Player p : party.getOnlinePlayers()) {
            expeditionPlayers.remove(p);
            playerPartyMap.remove(p.getUniqueId());
        }

        partyExpeditionMap.remove(party);
        expeditions.remove(toEnd);
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
        for (RoomBoundingBox box : roomData.roomBoundingBoxes()) {
            if (newPosition.isInAABB(box.minimum().toVector(), box.maximum().toVector())) {
                oldRoom.onPlayerMove(player, to);
                return;
            }
        }

        // If not, figure out what room they're in now
        try {
            // Grab the new room
            final var newRoomData = findRoomWithPlayer(expedition, newPosition);
            final var newRoom = newRoomData.room();
            // Let the expedition know that it changed
            expedition.setPlayerRoom(player, newRoomData);
            // Let the rooms know that it changed
            oldRoom.onPlayerExitRoom(player);
            if (oldRoom.getPlayers().size() < 1)
                oldRoom.onLastPlayerExitRoom(player);

            if (newRoom.getPlayers().size() < 1)
                newRoom.onFirstPlayerEnterRoom(player);
            newRoom.onPlayerEnterRoom(player);
            newRoom.onPlayerMove(player, to);
        } catch (InvalidExpeditionStateException e) {
            quitExpedition(player);
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
            for (RoomBoundingBox box : roomData.roomBoundingBoxes()) {
                if (!playerLocation.isInAABB(box.minimum().toVector(), box.maximum().toVector()))
                    continue;

                return roomData;
            }
        }

        // If we couldn't find one throw an exception
        throw new InvalidExpeditionStateException("unable to find the room that a player is in");
    }

    public void quitExpedition(@NotNull Player player) {
        final var party = playerPartyMap.get(player.getUniqueId());
        assert party != null;
        final var expedition = partyExpeditionMap.get(party);
        expedition.end();

        for (Player p : party.getOnlinePlayers()) {
            expeditionPlayers.remove(p);
            playerPartyMap.remove(p.getUniqueId());
        }

        partyExpeditionMap.remove(party);
        expeditions.remove(expedition);
    }
}
