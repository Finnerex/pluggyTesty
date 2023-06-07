package co.tantleffbeef.pluggytesty.expeditions.loading.roomloading;

import co.tantleffbeef.pluggytesty.expeditions.loading.RoomDoor;
import co.tantleffbeef.pluggytesty.expeditions.loading.RoomInformationInstance;
import co.tantleffbeef.pluggytesty.expeditions.loading.RoomInformation;
import co.tantleffbeef.pluggytesty.expeditions.loading.RoomInformationCollection;
import com.google.gson.JsonObject;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import java.util.*;

public class RandomRoomLoader implements RoomLoader {
    // TODO: remove the room_size thing
    private final static int ROOM_SIZE = 25;

    public static RoomLoader from(@NotNull JsonObject json, @NotNull RoomInformationCollection collection) {
        final var requiredRoomList = new ArrayList<RoomInformation>();
        final var optionalRoomList = new ArrayList<>();

        return new RandomRoomLoader(null, null, null, null, 0);
    }

    private static class RandomRoomDoor {
        public final Vector3i offset;
        public final RoomInformationInstance room;
        public final RoomDoor door;

        public RandomRoomDoor(Vector3i offset, RoomInformationInstance room, RoomDoor door) {
            this.offset = offset;
            this.room = room;
            this.door = door;
        }
    }

    private final RoomInformation firstRoom;
    private final RoomInformation lastRoom;
    private final List<RoomInformation> requiredRooms;
    private final List<RoomInformation> optionalRooms;
    private final int numOptional;

    /**
     * Creates a random room loader with the requested room
     * @param startingRoom the first room in the expedition
     * @param endingRoom the last room in the expedition
     * @param requiredRooms all rooms that 1 of each is required to spawn
     * @param optionalRooms rooms that will be picked from randomly to
     *                      fill all other rooms
     * @param numOptional the number of rooms to have more than the number
     *                    of required rooms
     * @throws IllegalArgumentException if any rooms do not have a list of doors
     */
    public RandomRoomLoader(@NotNull RoomInformation startingRoom,
                            @NotNull RoomInformation endingRoom,
                            @NotNull List<RoomInformation> requiredRooms,
                            @NotNull List<RoomInformation> optionalRooms,
                            int numOptional) {
        this.firstRoom = startingRoom;
        this.lastRoom = endingRoom;
        this.requiredRooms = requiredRooms;
        this.optionalRooms = optionalRooms;
        this.numOptional = numOptional;

        checkRooms();
    }

    private void checkRooms() {
        if (firstRoom.getDoors() == null)
            throw new IllegalArgumentException("Starting room does not have a doors list");

        if (lastRoom.getDoors() == null)
            throw new IllegalArgumentException("Ending room does not have a doors list");

        for (int i = 0; i < requiredRooms.size(); i++) {
            if (requiredRooms.get(i).getDoors() == null)
                throw new IllegalArgumentException("Required room " + i + " does not have a doors list");
        }

        for (int i = 0; i < optionalRooms.size(); i++) {
            if (optionalRooms.get(i).getDoors() == null)
                throw new IllegalArgumentException("Optional room " + i + " does not have a doors list");
        }
    }

    @Override
    public @NotNull List<RoomInformationInstance> loadRooms(int seed) {
        // Create a random for rng
        final var random = new Random(seed);

        // Create roomList and add all required rooms
        final var roomList = new ArrayList<>(requiredRooms);

        // now randomly add the right number of optional rooms
        for (int i = 0; i < numOptional; i++)
            roomList.add(optionalRooms.get(random.nextInt(optionalRooms.size())));

        // scramble the rooms
        Collections.shuffle(roomList, random);

        // generate rooms randomly

        // create roominfo instance but offset is from start
        // afterwards will recreate all but with new offset
        final Map<Vector3ic, RoomInformationInstance> roomsOffsetFromStart = new HashMap<>();
        final List<RandomRoomDoor> doors = new ArrayList<>();
        doors.add(null);

        // first, create first room
        addRoom(
                roomsOffsetFromStart,
                doors,
                new RoomInformationInstance(firstRoom, new Vector3i(), 0),
                ROOM_SIZE
        );

        // Then randomly generate
        for (RoomInformation room : roomList) {
            // generate instance
            final var roomInstance = pickLocationAndGenerateInstance(room, random, doors);

            // add the room
            addRoom(roomsOffsetFromStart, doors, roomInstance, ROOM_SIZE);
        }

        // lastly make final room


        return null;
    }

    private RoomInformationInstance pickLocationAndGenerateInstance(RoomInformation room, Random random, List<RandomRoomDoor> doors) {
        final var roomDoors = room.getDoors();

        // The door that already exists that we will connect to
        final int existingDoorIndex = random.nextInt(doors.size());
        final RandomRoomDoor existingDoor = doors.get(existingDoorIndex);

        // The door that we are going to connect to the door that exists
        assert roomDoors != null;
        final RoomDoor newDoor = roomDoors.get(random.nextInt(roomDoors.size()));

        // Calculate if this door needs to be rotated
        final double yaw = calculateYaw(existingDoor, newDoor);
        final Vector3i offset = calculateOffset(existingDoor.room,
                existingDoor.door.getDirection(),
                room,
                ROOM_SIZE);

        return new RoomInformationInstance(room, offset, yaw);
    }

    private double calculateYaw(@NotNull RandomRoomDoor existingDoor, @NotNull RoomDoor newDoor) {
        final var existingDoorDirection = existingDoor.door.getDirection();
        final var newDoorDirection = newDoor.getDirection();

        final double existingDoorAngle = calculateAngle(existingDoorDirection);
        final double newDoorAngle = calculateAngle(newDoorDirection);

        final double angleBetween = newDoorAngle - existingDoorAngle;

        // We want the two doors to be facing each other, so they should have a 180 degree difference
        return 180 - angleBetween;
    }

    private Vector3i calculateOffset(@NotNull RoomInformationInstance existingRoom,
                                     @NotNull BlockFace newRoomDirection,
                                     @NotNull RoomInformation newRoom,
                                     int roomSize) {
        final var existingRoomOffset = existingRoom.getOffset();
        final var existingRoomDoorHeightOffset = existingRoom.getRoomInformation().getHeightOffset();

        final var newRoomDoorHeighOffset = newRoom.getHeightOffset();

        // calculate where the new room will be on the y
        final int y = existingRoomOffset.y() + existingRoomDoorHeightOffset - newRoomDoorHeighOffset;

        int x = existingRoomOffset.x();
        int z = existingRoomOffset.z();

        switch (newRoomDirection) {
            case SOUTH -> z += roomSize;
            case NORTH -> z -= roomSize;
            case EAST -> x += roomSize;
            case WEST -> x -= roomSize;
        }

        return new Vector3i(x, y, z);
    }

    /**
     * Calculates the counter-clockwise angle from east (so east = 0) in degrees
     * but only for cardinal directions
     * @param direction the direction in question
     * @return read above
     */
    private static double calculateAngle(@NotNull BlockFace direction) {
        return switch (direction) {
            case EAST -> 0;
            case NORTH -> 90;
            case WEST -> 180;
            case SOUTH -> 270;
            default -> throw new IllegalArgumentException("direction not cardinal");
        };
    }

    private void addRoom(@NotNull Map<Vector3ic, RoomInformationInstance> roomsOffsetFromStart,
                         @NotNull List<RandomRoomDoor> doors,
                         @NotNull RoomInformationInstance newRoom,
                         int roomSize) {
        assert !roomsOffsetFromStart.containsKey(newRoom.getOffset());

        // TODO
    }
}
