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
    void build(@NotNull BukkitScheduler scheduler, @NotNull Location location, @NotNull Consumer<Expedition> postBuildCallback);

    /**
     * Starts the expedition with this party's players
     * @param party the party that will join the expedition
     */
    void start(@NotNull Party party);

    /**
     * Returns whether this expedition has been built
     * @return if this expedition is built
     */
    boolean isBuilt();

    /**
     * Returns the party of players that is in this expedition
     *
     * @return the party of players in this expedition
     */
    Party getParty();
}
