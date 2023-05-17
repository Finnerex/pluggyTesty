package co.tantleffbeef.pluggytesty.expeditions.rooms;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface StartingRoom extends Room {
    /**
     * Returns an array of potential starting
     * locations for players
     * @return array of starting locations
     */
    @NotNull Location[] getStartingLocations();
}
