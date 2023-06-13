package co.tantleffbeef.pluggytesty.expeditions;

import co.tantleffbeef.pluggytesty.expeditions.loading.ExpeditionInformation;
import co.tantleffbeef.pluggytesty.misc.Debug;
import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.BlockVector3Imp;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
import org.joml.Vector3i;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class ExpeditionBuilder {
    private final ExpeditionController expeditionController;
    private final World world;
    private final LocationTraverser locationTraverser;
    private final int maxExpeditionSize;

    public ExpeditionBuilder(@NotNull ExpeditionController expeditionController,
                             @NotNull Server server,
                             @NotNull String worldName,
                             @NotNull LocationTraverser locationTraverser,
                             int maxExpeditionSize) {
        this.expeditionController = expeditionController;
        this.world = setupExpeditionWorld(server, worldName);
        this.locationTraverser = locationTraverser;
        this.maxExpeditionSize = maxExpeditionSize;
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

    /**
     * Automatically allocates a location to build the
     * expedition, and starts the building process
     * @param buildInfo the information to use
     *                  to build the expedition
     */
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
            final var roomInfoList = buildInfo.getRoomLoader().loadRooms(new Random().nextInt());
            // make a vector to mark the minimum position
            // so we can adjust the whole room over
            final var minimumPos = new Vector3i(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

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

            return buildInfo.getExpeditionType().getConstructor().construct(
                    expeditionController,
                    expeditionLocationAsBukkitLocation,
                    roomMetadataList.toArray(new RoomMetadata[0])
            );
        });
    }

    private @NotNull Location bukkitLocationFromWE(@NotNull World world, @NotNull BlockVector3 pos) {
        return new Location(world, pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
    }
}
