package co.tantleffbeef.pluggytesty.expeditions;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface ExpeditionManager {
    /**
     * Starts an expedition. This expedition should already
     * be built.
     * @param builtExpedition a built expedition that should
     *                        now be started
     */
    void startExpedition(@NotNull Expedition builtExpedition, @NotNull Party party);

    /**
     * Automatically allocates a location to build the
     * expedition, and starts the building process
     * @param expedition the expedition to build
     * @param postBuildCallback a callback that will be
     *                          run after the expedition
     *                          finishes building
     */
    void buildExpedition(@NotNull Expedition expedition, Consumer<Expedition> postBuildCallback, @NotNull Consumer<Exception> errorCallback);

    /**
     * Returns whether the player is in an expedition currently
     * @param player the player to check
     * @return whether they are in an expedition
     */
    boolean inExpedition(@NotNull Player player);
}
