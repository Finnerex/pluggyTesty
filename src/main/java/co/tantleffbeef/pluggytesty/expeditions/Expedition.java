package co.tantleffbeef.pluggytesty.expeditions;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public interface Expedition {
    /**
     * Builds an expedition of this type at location
     * @param location where to build it
     * @param postBuildCallback a callback that runs once its finished
     */
    void build(@NotNull BukkitScheduler scheduler, @NotNull Location location, @NotNull Consumer<Room[]> postBuildCallback);

    /**
     * Starts the expedition with this party's players
     * @param party the party that will join the expedition
     */
    void start(@NotNull Party party);

    /**
     * Clears all chunks that the expedition take up
     * Precondition: this expedition has been built
     */
    void delete();
}
