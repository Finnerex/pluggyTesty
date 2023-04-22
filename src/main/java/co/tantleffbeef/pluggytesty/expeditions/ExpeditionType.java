package co.tantleffbeef.pluggytesty.expeditions;

import org.bukkit.Location;

import java.util.function.Consumer;

public interface ExpeditionType {
    void build(Location location, Consumer<Room[]> postBuildCallback);
}
