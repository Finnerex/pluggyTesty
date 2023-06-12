package co.tantleffbeef.pluggytesty.expeditions.loading.roomloading;

import co.tantleffbeef.pluggytesty.expeditions.loading.RoomDoor;
import co.tantleffbeef.pluggytesty.expeditions.loading.RoomInformationInstance;
import co.tantleffbeef.pluggytesty.expeditions.loading.RoomInformation;
import co.tantleffbeef.pluggytesty.expeditions.loading.RoomInformationCollection;
import co.tantleffbeef.pluggytesty.misc.Debug;
import com.google.gson.JsonObject;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import java.util.*;

public class RandomRoomLoader implements RoomLoader {
    // TODO: remove the room_size thing
    private final static int ROOM_SIZE = 25;

    public static @NotNull RoomLoader from(@NotNull JsonObject json, @NotNull RoomInformationCollection collection) {
        final var requiredRoomList = new ArrayList<RoomInformation>();
        final var optionalRoomList = new ArrayList<>();

        return new RandomRoomLoader(null, null, null, null, 0);
    }

    private static class RandomRoomDoor {
        public final RoomInformationInstance room;
        public final RoomDoor door;

        public RandomRoomDoor(RoomInformationInstance room, RoomDoor door) {
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
    public @NotNull Collection<RoomInformationInstance> loadRooms(int seed) {
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
        final List<RoomInformationInstance> roomsList = new ArrayList<>();
        final List<RandomRoomDoor> doors = new ArrayList<>();

        // first, create first room
        assert firstRoom.getDoors() != null;
        addRoom(
                roomsOffsetFromStart,
                roomsList,
                doors,
                new RoomInformationInstance(firstRoom, firstRoom.getDoors(), new Vector3i(), 0),
                ROOM_SIZE
        );

        // Then randomly generate
        for (RoomInformation room : roomList) {
            // generate instance
            final var roomInstance = pickLocationAndGenerateInstance(room, random, doors);

            // add the room
            addRoom(roomsOffsetFromStart, roomsList, doors, roomInstance, ROOM_SIZE);
        }

        // lastly make final room
        final var finalRoomInstance = pickLocationAndGenerateInstance(lastRoom, random, doors);
        addRoom(roomsOffsetFromStart, roomsList, doors, finalRoomInstance, ROOM_SIZE);

        return roomsOffsetFromStart.values();
    }

    private RoomInformationInstance pickLocationAndGenerateInstance(RoomInformation room, Random random, List<RandomRoomDoor> doors) {
        final var roomDoors = room.getDoors();

        // The door that already exists that we will connect to
        final int existingDoorIndex = random.nextInt(doors.size());
        Debug.info("pickLocationAndGenerateInsance(): doors.size() = " + doors.size());
        Debug.info("pickLocationAndGenerateInsance(): existingDoorIndex = " + existingDoorIndex);
        final RandomRoomDoor existingDoor = doors.get(existingDoorIndex);
        if (existingDoor == null)
            Debug.info("pickLocationAndGenerateInsance(): existingDoor = null");

        // The door that we are going to connect to the door that exists
        assert roomDoors != null;
        final RoomDoor newDoor = roomDoors.get(random.nextInt(roomDoors.size()));
        Debug.info("pickLocationAndGenerateInsance(): roomDoors.size() = " + roomDoors.size());

        // Calculate if this door needs to be rotated
        assert existingDoor != null;
        assert newDoor != null;
        final int yaw = calculateYaw(existingDoor, newDoor);
        final Vector3i offset = calculateOffset(existingDoor.room,
                existingDoor.door.getDirection(),
                room,
                ROOM_SIZE);

        final List<RoomDoor> rotatedDoors = new ArrayList<>();
        roomDoors.forEach(door -> rotatedDoors.add(new RoomDoor(rotateFace(door.getDirection(), yaw), door.getReplacementMaterial())));

        return new RoomInformationInstance(room, rotatedDoors, offset, yaw);
    }

    private int calculateYaw(@NotNull RandomRoomDoor existingDoor, @NotNull RoomDoor newDoor) {
        final var existingDoorDirection = existingDoor.door.getDirection();
        final var newDoorDirection = newDoor.getDirection();

        final int existingDoorAngle = calculateAngle(existingDoorDirection);
        final int newDoorAngle = calculateAngle(newDoorDirection);

        final int angleBetween = newDoorAngle - existingDoorAngle;

        // We want the two doors to be facing each other, so they should have a 180 degree difference
        return 180 - angleBetween;
    }

    private @NotNull BlockFace rotateFace(@NotNull BlockFace original, int theta) {
        final int originalDirection = calculateAngle(original);

        final int newDirection = (originalDirection + (360 + theta)) % 360;

        return findBlockFace(newDirection);
    }

    private @NotNull BlockFace findBlockFace(int theta) {
        return switch (theta) {
            case 0 -> BlockFace.EAST;
            case 90 -> BlockFace.NORTH;
            case 180 -> BlockFace.WEST;
            case 270 -> BlockFace.SOUTH;
            default -> throw new IllegalStateException("idk what happened man");
        };
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
    private static int calculateAngle(@NotNull BlockFace direction) {
        return switch (direction) {
            case EAST -> 0;
            case NORTH -> 90;
            case WEST -> 180;
            case SOUTH -> 270;
            default -> throw new IllegalArgumentException("direction not cardinal");
        };
    }

    private void addRoom(@NotNull Map<Vector3ic, RoomInformationInstance> roomsOffsetFromStart,
                         @NotNull List<RoomInformationInstance> roomsList,
                         @NotNull List<RandomRoomDoor> doors,
                         @NotNull RoomInformationInstance newRoom,
                         int roomSize) {
        //assert !roomsOffsetFromStart.containsKey(newRoom.getOffset()); TODO
        if (roomsOffsetFromStart.containsKey(newRoom.getOffset())) {
            Debug.error("roomsOffsetFromStart contains offset " + newRoom.getOffset() + " already");
            Debug.error("original: " + roomsOffsetFromStart.get(newRoom.getOffset()).getRoomInformation().schematicPath);
            Debug.error("new:      " + newRoom.getRoomInformation().schematicPath);
        }

        // first add the room to the list of rooms
        final var offset = newRoom.getOffset();
        Debug.info("addRoom(): offset = " + offset);
        roomsOffsetFromStart.put(offset, newRoom);
        roomsList.add(newRoom);

        // then add or remove the corresponding doors

        // find the rotated directions of all the room doors
        final Map<BlockFace, RandomRoomDoor> doorDirections = new HashMap<>();
        assert newRoom.getDoors() != null;
        newRoom.getDoors().forEach(door -> doorDirections.put(door.getDirection(), new RandomRoomDoor(newRoom, door)));

        // North
        final var northOffset = new Vector3i(offset).add(0, 0, -roomSize);
        if (roomsOffsetFromStart.containsKey(northOffset)) {
            Debug.info("north offset found");
            final var northRoom = roomsOffsetFromStart.get(northOffset);
            if (northRoom == null)
                throw new IllegalStateException("bruh");

            final var northRoomDoors = northRoom.getDoors();
            if (northRoomDoors != null) {
                final var northRoomSouthDoor = northRoomDoors.stream().filter(door -> door.getDirection().equals(BlockFace.SOUTH)).findAny();
                if (northRoomSouthDoor.isPresent()) {
                    final var removalResult = doors.remove(new RandomRoomDoor(northRoom, northRoomSouthDoor.get()));
                    if (!removalResult) Debug.warn("removalResult is false at offset = " + northOffset);
                }
            }
        } else if (doorDirections.containsKey(BlockFace.NORTH)) {
            doors.add(doorDirections.get(BlockFace.NORTH));
        }

        // South
        final var southOffset = new Vector3i(offset).add(0, 0, roomSize);
        if (roomsOffsetFromStart.containsKey(southOffset)) {
            final var southRoom = roomsOffsetFromStart.get(southOffset);
            if (southRoom == null)
                throw new IllegalStateException("bruh");

            final var southRoomDoors = southRoom.getDoors();
            if (southRoomDoors != null) {
                final var southRoomNorthDoor = southRoomDoors.stream().filter(door -> door.getDirection().equals(BlockFace.NORTH)).findAny();
                if (southRoomNorthDoor.isPresent()) {
                    final var removalResult = doors.remove(new RandomRoomDoor(southRoom, southRoomNorthDoor.get()));
                    if (!removalResult) Debug.warn("removalResult is false at offset = " + southOffset);
                }
            }
        } else if (doorDirections.containsKey(BlockFace.SOUTH)) {
            doors.add(doorDirections.get(BlockFace.SOUTH));
        }

        // East
        final var eastOffset = new Vector3i(offset).add(roomSize, 0, 0);
        if (roomsOffsetFromStart.containsKey(eastOffset)) {
            final var eastRoom = roomsOffsetFromStart.get(eastOffset);
            if (eastRoom == null)
                throw new IllegalStateException("bruh");

            final var eastRoomDoors = eastRoom.getDoors();
            if (eastRoomDoors != null) {
                final var eastRoomWestDoor = eastRoomDoors.stream().filter(door -> door.getDirection().equals(BlockFace.WEST)).findAny();
                if (eastRoomWestDoor.isPresent()) {
                    final var removalResult = doors.remove(new RandomRoomDoor(eastRoom, eastRoomWestDoor.get()));
                    if (!removalResult) Debug.warn("removalResult is false at offset = " + eastOffset);
                }
            }
        } else if (doorDirections.containsKey(BlockFace.EAST)) {
            doors.add(doorDirections.get(BlockFace.EAST));
        }

        // West
        final var westOffset = new Vector3i(offset).add(-roomSize, 0, 0);
        if (roomsOffsetFromStart.containsKey(westOffset)) {
            final var westRoom = roomsOffsetFromStart.get(westOffset);
            if (westRoom == null)
                throw new IllegalStateException("bruh");

            final var westRoomDoors = westRoom.getDoors();
            if (westRoomDoors != null) {
                final var westRoomEastDoor = westRoomDoors.stream().filter(door -> door.getDirection().equals(BlockFace.EAST)).findAny();
                if (westRoomEastDoor.isPresent()) {
                    final var removalResult = doors.remove(new RandomRoomDoor(westRoom, westRoomEastDoor.get()));
                    if (!removalResult) Debug.warn("removalResult is false at offset = " + westOffset);
                }
            }
        } else if (doorDirections.containsKey(BlockFace.WEST)) {
            doors.add(doorDirections.get(BlockFace.WEST));
        }
    }
}
