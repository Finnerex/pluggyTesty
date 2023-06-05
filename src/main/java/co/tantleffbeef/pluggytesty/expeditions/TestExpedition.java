package co.tantleffbeef.pluggytesty.expeditions;

import co.tantleffbeef.pluggytesty.expeditions.parties.Party;
import co.tantleffbeef.pluggytesty.expeditions.rooms.StartingRoom;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TestExpedition implements Expedition {
    private final ExpeditionManager manager;
    private final Map<UUID, RoomMetadata> playerRoomMap;
    private final Location minimumCorner;
    private final RoomMetadata[] rooms;
    private Party party;

    public TestExpedition(@NotNull ExpeditionManager manager, @NotNull Location minimumCorner, @NotNull RoomMetadata[] rooms) {
        this.manager = manager;
        this.minimumCorner = minimumCorner;
        this.rooms = rooms;

        this.playerRoomMap = new HashMap<>();

        for (var roomData : rooms)
            roomData.room().setExpedition(this);
    }

    //@Override
    /*public void calculateMinimumPointDistanceFromPasteLocation(@NotNull BukkitScheduler scheduler, @NotNull Consumer<Vector3ic> postCalculationCallback) {
        /*scheduler.runTaskAsynchronously(schedulerPlugin, () -> {
            // Load the schematic using FaweAPI
            try (final var schematic = FaweAPI.load(schedulerPlugin.getDataFolder().toPath().resolve("testexpedition.schem").toFile())) {
                // just return where the minimum point is relative to
                final var minimum = schematic.getOrigin();
                scheduler.runTask(schedulerPlugin, () -> postCalculationCallback.accept(new Vector3i(minimum.getBlockX(), minimum.getBlockY(), minimum.getBlockZ())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });* /
        postCalculationCallback.accept(null);
    }*/

    //@Override
    /*public void build(@NotNull ExpeditionManager manager, @NotNull BukkitScheduler scheduler, @NotNull Location location, @NotNull Consumer<Expedition> postBuildCallback, @NotNull Consumer<Exception> errorCallback) {
        assert location.getWorld() != null;

        final var world = location.getWorld();

        builtLocation = location;

        // Paste the schematic async
        scheduler.runTaskAsynchronously(schedulerPlugin, () -> {
            // Load the schematic using FaweAPI
            try (final var schematic = FaweAPI.load(schedulerPlugin.getDataFolder().toPath().resolve("testexpedition.schem").toFile())) {
                schematic.setOrigin(schematic.getMinimumPoint());

                // Paste it once its loaded
                final EditSession pasteSession = schematic.paste(
                        FaweAPI.getWorld(world.getName()),
                        BlockVector3Imp.at(location.getBlockX(), location.getBlockY(), location.getBlockZ())
                );

                // mark expedition as built
                built = true;

                final var dimensions = schematic.getDimensions();

                // fill rooms array
                // 7 1 -12, 8 8 9, 23 8 -85
                rooms = new RoomMetadata[] {
                        new RoomMetadata(
                                new SimpleStartingRoom(
                                        new Location[] {
                                                location.clone().add(23, 9, 16),
                                                location.clone().add(13, 1, 13),
                                                location.clone().add(7, 5, 19),
                                                location.clone().add(7, 9, 8),
                                                location.clone().add(21, 6, 6),
                                        },
                                        manager
                                ),
                                new RoomBoundingBox(
                                        location.clone(),
                                        location.clone().add(dimensions.getBlockX(), dimensions.getBlockY(), dimensions.getBlockZ())
                                )
                        )
                };

                // After done pasting, run the callback on the main thread
                scheduler.runTask(schedulerPlugin, () -> postBuildCallback.accept(this));
            } catch (IOException e) {
                errorCallback.accept(e);
            }
        });
    }*/

    /**
     * Converts a blockvector3 into a bukkit location
     * using the Block X, Y, and Z (so based on integers)
     * @param world the world to pass into this location
     * @param vec3 the vector to turn into a location
     * @return the generated location
     */
    private static Location toBukkitBlockLoc(@NotNull World world, @NotNull BlockVector3 vec3) {
        return new Location(
                world,
                vec3.getBlockX(),
                vec3.getBlockY(),
                vec3.getBlockZ()
        );
    }

    @Override
    public void start(@NotNull Party party) {
        // Save the party
        this.party = party;

        // Grab the first room
        assert rooms != null && rooms.length > 0;
        final var startingRoomMeta = rooms[0];
        // Make sure its actually a starting room
        assert startingRoomMeta.room() instanceof StartingRoom;
        final var startingRoom = (StartingRoom) startingRoomMeta.room();

        // Grab all the players that are in the expedition
        final var partyPlayers = party.getOnlinePlayers().toArray(new Player[0]);
        // Grab the potential roomBoundingBoxes they can be sent to
        startingRoom.addInitialPlayers(List.of(partyPlayers));
        Arrays.stream(partyPlayers).forEach(player -> playerRoomMap.put(player.getUniqueId(), startingRoomMeta));

        // start the first room
        startingRoom.initialRoomStartup();

        // TODO: finish
    }

    /*@Override
    public boolean isBuilt() {
        return built;
    }*/

    @Override
    public @NotNull RoomMetadata[] getRooms() {
        return rooms;
    }

    @Override
    public @NotNull Party getParty() {
        return party;
    }

    @Override
    public @NotNull RoomMetadata getRoomWithPlayerData(@NotNull Player player) {
        assert playerRoomMap.containsKey(player.getUniqueId());

        return playerRoomMap.get(player.getUniqueId());
    }

    @Override
    public void setPlayerRoom(@NotNull Player player, @NotNull RoomMetadata room) {
        playerRoomMap.remove(player.getUniqueId());
        playerRoomMap.put(player.getUniqueId(), room);
    }

    @Override
    public void end() {
        for (RoomMetadata r : rooms) {
            final var room = r.room();
            if (!room.hasPlayers())
                continue;

            final var playerList = new ArrayList<>(room.getPlayers());

            playerList.forEach(room::onPlayerExitRoom);
            room.onLastPlayerExitRoom(playerList.get(0));
        }

        party.broadcastMessage("exited expedition");

        manager.endExpedition(this);
    }
}
