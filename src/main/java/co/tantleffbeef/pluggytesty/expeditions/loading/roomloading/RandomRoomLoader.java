package co.tantleffbeef.pluggytesty.expeditions.loading.roomloading;

import co.tantleffbeef.pluggytesty.expeditions.loading.*;
import co.tantleffbeef.pluggytesty.misc.Debug;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import java.util.*;

import static co.tantleffbeef.pluggytesty.misc.BlockFaceMath.*;

public class RandomRoomLoader implements RoomLoader {

    private static class RandomRoomDoor {
        public final RoomInformationInstance room;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RandomRoomDoor that = (RandomRoomDoor) o;
            return room.equals(that.room) && door.equals(that.door);
        }

        @Override
        public int hashCode() {
            return Objects.hash(room, door);
        }

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
    private final int roomSize;
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
                            int roomSize,
                            int numOptional) {
        this.firstRoom = startingRoom;
        this.lastRoom = endingRoom;
        this.requiredRooms = requiredRooms;
        this.optionalRooms = optionalRooms;
        this.roomSize = roomSize;
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
    public String toString() {
        return "RandomRoomLoader{" +
                "firstRoom=" + firstRoom +
                ", lastRoom=" + lastRoom +
                ", requiredRooms=" + requiredRooms +
                ", optionalRooms=" + optionalRooms +
                ", roomSize=" + roomSize +
                ", numOptional=" + numOptional +
                '}';
    }

    @Override
    public @NotNull Collection<RoomInformationInstance> loadRooms(int seed) {
        // Create a random for rng - kys finnerex you should die and suck cock
        final var random = new Random(seed);

        // Create roomList and add all required rooms
        // final var roomList = new ArrayList<>(requiredRooms);
        final var optionalRoomList = new ArrayList<RoomInformation>();
        final var requiredRoomList = new ArrayList<>(requiredRooms);

        // now randomly add the right number of optional rooms
        for (int i = 0; i < numOptional; i++)
            optionalRoomList.add(optionalRooms.get(random.nextInt(optionalRooms.size())));

        // scramble the rooms
        Collections.shuffle(optionalRoomList, random);
        Collections.shuffle(requiredRoomList, random);

        // generate rooms randomly

        // create roominfo instance but offset is from start
        final Map<Vector2ic, RoomInformationInstance> roomsOffsetFromStart = new HashMap<>();
        final List<RoomInformationInstance> roomsList = new ArrayList<>();
        final List<RandomRoomDoor> doors = new ArrayList<>();

        // first, create first room
        assert firstRoom.getDoors() != null;
        addRoom(
                roomsOffsetFromStart,
                roomsList,
                doors,
                new RoomInformationInstance(firstRoom, firstRoom.getDoors(), new Vector3i(), 0, 0)
        );

        // Then randomly generate
        for (RoomInformation room : optionalRoomList) {
            // generate instance
            final var roomInstance = pickLocationAndGenerateInstance(room, random, doors);

            // add the room
            addRoom(roomsOffsetFromStart, roomsList, doors, roomInstance);
        }

        for (RoomInformation room : requiredRoomList) {
            final var roomInstance = pickLocationAndGenerateInstance(room, random, doors);

            addRoom(roomsOffsetFromStart, roomsList, doors, roomInstance);
        }

        // lastly make final room
        final var finalRoomInstance = pickLocationAndGenerateInstance(lastRoom, random, doors);
        addRoom(roomsOffsetFromStart, roomsList, doors, finalRoomInstance);

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
                existingDoor.door.getHeightOffset(random.nextInt()),
                newDoor.getHeightOffset(random.nextInt())
        );

        final List<RoomDoor> rotatedDoors = new ArrayList<>();
        roomDoors.forEach(door -> rotatedDoors.add(new ConsistentHeightRoomDoor(rotateFace(door.getDirection(), yaw), door.getReplacementMaterial(), door.getHeightOffset(random.nextInt()))));

        return new RoomInformationInstance(room, rotatedDoors, offset, yaw, random.nextInt());
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

    private Vector3i calculateOffset(@NotNull RoomInformationInstance existingRoom,
                                     @NotNull BlockFace newRoomDirection,
                                     int existingDoorHeightOffset,
                                     int newDoorHeightOffset) {
        final var existingRoomOffset = existingRoom.getOffset();

        // calculate where the new room will be on the y
        final int y = existingRoomOffset.y() + existingDoorHeightOffset - newDoorHeightOffset;

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

    private void addRoom(@NotNull Map<Vector2ic, RoomInformationInstance> roomsOffsetFromStart,
                         @NotNull List<RoomInformationInstance> roomsList,
                         @NotNull List<RandomRoomDoor> doors,
                         @NotNull RoomInformationInstance newRoom) {
        //assert !roomsOffsetFromStart.containsKey(newRoom.getOffset()); TODO
        final Vector3ic offset = newRoom.getOffset();
        final var offset2d = new Vector2i(offset.x(), offset.z());
        // check if a room at this x and z already exists
        if (roomsOffsetFromStart.containsKey(offset2d)) {
            Debug.error("roomsOffsetFromStart contains offset " + offset2d + " already");
            Debug.error("original: " + roomsOffsetFromStart.get(offset2d).getRoomInformation().schematicPath);
            Debug.error("new:      " + newRoom.getRoomInformation().schematicPath);
        }

        // first add the room to the list of rooms
        Debug.info("addRoom(): offset = " + offset + ", angle = " + newRoom.getRotation());
        roomsOffsetFromStart.put(offset2d, newRoom);
        roomsList.add(newRoom);

        // then add or remove the corresponding doors

        // find the rotated directions of all the room doors
        final Map<BlockFace, RandomRoomDoor> doorDirections = new HashMap<>();
        newRoom.getDoors().forEach(door -> doorDirections.put(door.getDirection(), new RandomRoomDoor(newRoom, door)));

        // North
        final var northOffset = new Vector2i(offset2d).add(0, -roomSize);
        if (roomsOffsetFromStart.containsKey(northOffset)) {
            Debug.info("north offset found");
            final var northRoom = roomsOffsetFromStart.get(northOffset);
            if (northRoom == null)
                throw new IllegalStateException("bruh");

            final var northRoomDoors = northRoom.getDoors();
            if (!northRoomDoors.isEmpty()) {
                final var northRoomSouthDoor = northRoomDoors.stream().filter(door -> door.getDirection().equals(BlockFace.SOUTH)).findAny();
                if (northRoomSouthDoor.isPresent()) {
                    final var removalResult = doors.remove(new RandomRoomDoor(northRoom, northRoomSouthDoor.get()));
                    if (!removalResult) Debug.warn("north removalResult is false at offset = " + northOffset);
                }
            }
        } else if (doorDirections.containsKey(BlockFace.NORTH)) {
            Debug.info("adding north door");
            doors.add(doorDirections.get(BlockFace.NORTH));
        }

        // South
        final var southOffset = new Vector2i(offset2d).add(0, roomSize);
        if (roomsOffsetFromStart.containsKey(southOffset)) {
            final var southRoom = roomsOffsetFromStart.get(southOffset);
            if (southRoom == null)
                throw new IllegalStateException("bruh");

            final var southRoomDoors = southRoom.getDoors();
            if (!southRoomDoors.isEmpty()) {
                final var southRoomNorthDoor = southRoomDoors.stream().filter(door -> door.getDirection().equals(BlockFace.NORTH)).findAny();
                if (southRoomNorthDoor.isPresent()) {
                    final var removalResult = doors.remove(new RandomRoomDoor(southRoom, southRoomNorthDoor.get()));
                    if (!removalResult) Debug.warn("south removalResult is false at offset = " + southOffset);
                }
            }
        } else if (doorDirections.containsKey(BlockFace.SOUTH)) {
            Debug.info("adding south door");
            doors.add(doorDirections.get(BlockFace.SOUTH));
        }

        // East
        final var eastOffset = new Vector2i(offset2d).add(roomSize, 0);
        if (roomsOffsetFromStart.containsKey(eastOffset)) {
            final var eastRoom = roomsOffsetFromStart.get(eastOffset);
            if (eastRoom == null)
                throw new IllegalStateException("bruh");

            final var eastRoomDoors = eastRoom.getDoors();
            if (!eastRoomDoors.isEmpty()) {
                final var eastRoomWestDoor = eastRoomDoors.stream().filter(door -> door.getDirection().equals(BlockFace.WEST)).findAny();
                if (eastRoomWestDoor.isPresent()) {
                    final var removalResult = doors.remove(new RandomRoomDoor(eastRoom, eastRoomWestDoor.get()));
                    if (!removalResult) Debug.warn("east removalResult is false at offset = " + eastOffset);
                }
            }
        } else if (doorDirections.containsKey(BlockFace.EAST)) {
            Debug.info("adding east door");
            doors.add(doorDirections.get(BlockFace.EAST));
        }

        // West
        final var westOffset = new Vector2i(offset2d).add(-roomSize, 0);
        if (roomsOffsetFromStart.containsKey(westOffset)) {
            final var westRoom = roomsOffsetFromStart.get(westOffset);
            if (westRoom == null)
                throw new IllegalStateException("bruh");

            final var westRoomDoors = westRoom.getDoors();
            if (!westRoomDoors.isEmpty()) {
                final var westRoomEastDoor = westRoomDoors.stream().filter(door -> door.getDirection().equals(BlockFace.EAST)).findAny();
                if (westRoomEastDoor.isPresent()) {
                    final var removalResult = doors.remove(new RandomRoomDoor(westRoom, westRoomEastDoor.get()));
                    if (!removalResult) Debug.warn("west removalResult is false at offset = " + westOffset);
                }
            }
        } else if (doorDirections.containsKey(BlockFace.WEST)) {
            Debug.info("adding west door");
            doors.add(doorDirections.get(BlockFace.WEST));
        }
    }
}
