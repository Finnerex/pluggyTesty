package co.tantleffbeef.pluggytesty.expeditions;

import co.tantleffbeef.pluggytesty.expeditions.loading.ExpeditionInformation;
import co.tantleffbeef.pluggytesty.expeditions.loading.PTRoomInformationCollection;
import co.tantleffbeef.pluggytesty.expeditions.loading.RoomInformationCollection;
import co.tantleffbeef.pluggytesty.misc.Debug;
import co.tantleffbeef.pluggytesty.misc.ErrorCode;
import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.BlockVector3Imp;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
import org.joml.Vector3i;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;

public class PTExpeditionManager implements ExpeditionManager {
    private final PartyManager partyManager;
    private final BukkitScheduler scheduler;
    private final RoomInformationCollection rooms;
    private final LocationTraverser locationTraverser;
    private final World world;
    private final Set<Player> expeditionPlayers;
    private final List<Expedition> expeditions;
    private final Map<Party, Expedition> partyExpeditionMap;
    private final Map<UUID, Party> playerPartyMap;
    private final int maxExpeditionSize;

    public PTExpeditionManager(@NotNull PartyManager partyManager,
                               @NotNull BukkitScheduler scheduler,
                               @NotNull Server server,
                               @NotNull String expeditionWorldName,
                               int maxExpeditionSize) {
        this.partyManager = partyManager;
        this.scheduler = scheduler;
        this.expeditionPlayers = new HashSet<>();
        this.expeditions = new ArrayList<>();
        this.partyExpeditionMap = new HashMap<>();
        this.playerPartyMap = new HashMap<>();
        this.world = setupExpeditionWorld(server, expeditionWorldName);
        this.locationTraverser = new LocationTraverser();
        this.maxExpeditionSize = maxExpeditionSize;

        rooms = new PTRoomInformationCollection();
    }

    /**
     * Tries to grab the world with worldName and if it doesn't exist
     * then it creates a new one
     * @param server the server object for creating the world with
     * @param worldName the name of the world to grab
     * @return the newly created world, hopefully
     */
    private static @NotNull World setupExpeditionWorld(@NotNull Server server, @NotNull String worldName) {
        final var worldCreator = new WorldCreator(worldName);
        // Make a new chunk generator that just returns void for everything
        worldCreator.generator(new ChunkGenerator() {});

        // This will either create a new world or just grab the world with this name

        return Objects.requireNonNull(server.createWorld(worldCreator));
    }

    @Override
    public void startExpedition(@NotNull Expedition builtExpedition, @NotNull Party party) {
        // Register the expedition
        registerExpedition(builtExpedition, party);

        // then start it
        builtExpedition.start(party);
    }

    @Override
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
            final var rooms1 = buildInfo.roomInformationList;
            // make a vector to mark the minimum position
            // so we can adjust the whole room over
            final var minimumPos = new Vector3i(0, 0, 0);

            // Loop through every room to find the minimum position
            for (var room : rooms1) {
//                final var info = room.roomInformation;
                final var offset = room.offset;

                if (offset.x < minimumPos.x)
                    minimumPos.x = offset.x;
                if (offset.y < minimumPos.y)
                    minimumPos.y = offset.y;
                if (offset.z < minimumPos.z)
                    minimumPos.z = offset.z;
            }

            // This offset is used so that the expedition's minimum position is put at the position
            // its given to build at
            final var expeditionLocationWithOffset = minimumPos.mul(-1, new Vector3i()).add(expeditionCorner.x, 0, expeditionCorner.y);

            // Create something to hold the room metadata for all the rooms
            final var roomMetadataList = new ArrayList<RoomMetadata>();

            // Loop through every room and paste it
            for (var room : rooms1) {
                final var info = room.roomInformation;
                final var roomOffset = room.offset;
                final var schemPath = info.schematicPath;

                // figure out where to paste the room
                final var pasteLocation = BlockVector3Imp.at(
                        expeditionLocationWithOffset.x + roomOffset.x,
                        expeditionLocationWithOffset.y + roomOffset.y,
                        expeditionLocationWithOffset.z + roomOffset.z
                );

                // This will store the location and stuff for this room
                final RoomMetadata roomData;

                try (final var schem = FaweAPI.load(schemPath.toFile())) {
                    // Set the origin to the minimum point so that it actually
                    // pastes it where you're trying to paste it
                    schem.setOrigin(schem.getMinimumPoint());

                    // Paste the room's schematic
                    final var pasteSession = schem.paste(weWorld, pasteLocation);

                    final var minimumPoint = pasteSession.getMinimumPoint();
                    final var maximumPoint = pasteSession.getMaximumPoint();

                    // store the room's data
                    final var roomObject = info.roomType.getConstructor().construct();
                    roomData = new RoomMetadata(
                            roomObject,
                            // Give the room a bounding box based solely on its schematic
                            new RoomBoundingBox(
                                    bukkitLocationFromWE(world, minimumPoint),
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

            return buildInfo.expeditionType.getConstructor().construct(
                    expeditionLocationAsBukkitLocation,
                    roomMetadataList.toArray(new RoomMetadata[0])
            );
        });
    }

    private @NotNull Location bukkitLocationFromWE(@NotNull World world, @NotNull BlockVector3 pos) {
        return new Location(world, pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
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
