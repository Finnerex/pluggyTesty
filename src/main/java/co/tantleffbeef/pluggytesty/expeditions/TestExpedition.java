package co.tantleffbeef.pluggytesty.expeditions;

import co.tantleffbeef.pluggytesty.expeditions.rooms.SimpleStartingRoom;
import co.tantleffbeef.pluggytesty.expeditions.rooms.StartingRoom;
import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.BlockVector3Imp;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class TestExpedition implements Expedition {
    private final Plugin schedulerPlugin;
    private Location builtLocation;
    private RoomMetadata[] rooms;
    private Party party;
    private boolean built;

    public TestExpedition(@NotNull Plugin schedulerPlugin) {
        this.schedulerPlugin = schedulerPlugin;
        built = false;
    }

    @Override
    public void calculateMinimumPointDistanceFromPasteLocation(@NotNull BukkitScheduler scheduler, @NotNull Consumer<Vector3ic> postCalculationCallback) {
        scheduler.runTaskAsynchronously(schedulerPlugin, () -> {
            // Load the schematic using FaweAPI
            try (final var schematic = FaweAPI.load(schedulerPlugin.getDataFolder().toPath().resolve("testexpedition.schem").toFile())) {
                // just return where the minimum point is relative to
                final var minimum = schematic.getMinimumPoint();
                scheduler.runTask(schedulerPlugin, () -> postCalculationCallback.accept(new Vector3i(minimum.getBlockX(), minimum.getBlockY(), minimum.getBlockZ())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void build(@NotNull BukkitScheduler scheduler, @NotNull Location location, @NotNull Consumer<Expedition> postBuildCallback, @NotNull Consumer<Exception> errorCallback) {
        assert location.getWorld() != null;

        final var world = location.getWorld();

        builtLocation = location;

        // Paste the schematic async
        scheduler.runTaskAsynchronously(schedulerPlugin, () -> {
            // Load the schematic using FaweAPI
            try (final var schematic = FaweAPI.load(schedulerPlugin.getDataFolder().toPath().resolve("testexpedition.schem").toFile())) {
                // Paste it once its loaded
                final EditSession pasteSession = schematic.paste(
                        FaweAPI.getWorld(world.getName()),
                        BlockVector3Imp.at(location.getBlockX(), location.getBlockY(), location.getBlockZ())
                );

                // mark expedition as built
                built = true;

                // fill rooms array
                // 7 1 -12, 8 8 9, 23 8 -85
                rooms = new RoomMetadata[] {
                        new RoomMetadata(
                                new SimpleStartingRoom(
                                        new Location[] {
                                                location.clone().add(7, 1, -12),
                                                location.clone().add(8, 8, 9),
                                                location.clone().add(23, 8, -85),
                                        }
                                ),
                                toBukkitBlockLoc(world, pasteSession.getMinimumPoint()),
                                toBukkitBlockLoc(world, pasteSession.getMaximumPoint())
                        )
                };

                // After done pasting, run the callback on the main thread
                scheduler.runTask(schedulerPlugin, () -> postBuildCallback.accept(this));
            } catch (IOException e) {
                errorCallback.accept(e);
            }
        });
    }

    private void loadSchematic() {

    }

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
        // Grab the potential locations they can be sent to
        final var startingLocations = startingRoom.getStartingLocations();

        // Teleport all of the players to the starting locations
        spreadPlayers(startingLocations, partyPlayers);

        // TODO: finish
    }

    private static void spreadPlayers(@NotNull Location[] locations, @NotNull Player[] players) {
        assert locations.length > 0;

        // Holds all potential locations a player could be sent to
        final List<Location> remainingLocations = new ArrayList<>();

        for (final var player : players) {
            // If we've run out of locations
            // then add them all back
            if (remainingLocations.size() < 1)
                remainingLocations.addAll(List.of(locations));

            // Pick a random location
            final var locationIndex = new Random().nextInt(remainingLocations.size());

            // Send the player there
            player.teleport(remainingLocations.get(locationIndex));

            // Remove it from the list of locations
            remainingLocations.remove(locationIndex);
        }
    }

    @Override
    public boolean isBuilt() {
        return built;
    }

    @Override
    public @NotNull RoomMetadata[] getRooms() {
        return rooms;
    }

    @Override
    public Party getParty() {
        return party;
    }

    @Override
    public @NotNull RoomMetadata getRoomWithPlayerData(@NotNull Player player) {
        return null;
    }

    @Override
    public void setPlayerRoom(@NotNull RoomMetadata room, @NotNull Player player) {

    }

    @Override
    public void end() {

    }
}
