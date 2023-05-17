package co.tantleffbeef.pluggytesty.expeditions;

import co.tantleffbeef.pluggytesty.expeditions.rooms.Room;
import org.bukkit.Location;

public record RoomMetadata(Room room, Location minimum, Location maximum) { }
