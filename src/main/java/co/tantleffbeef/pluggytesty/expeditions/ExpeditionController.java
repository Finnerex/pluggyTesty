package co.tantleffbeef.pluggytesty.expeditions;

import co.tantleffbeef.pluggytesty.expeditions.parties.Party;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ExpeditionController {
    /**
     * Starts an expedition. This expedition should already
     * be built.
     * @param builtExpedition a built expedition that should
     *                        now be started
     */
    void startExpedition(@NotNull Expedition builtExpedition, @NotNull Party party);

    /*/**
     * Automatically allocates a location to build the
     * expedition, and starts the building process
     * @param buildInfo the information to use
     *                  to build the expedition
     * /
    @NotNull CompletableFuture<Expedition> buildExpedition(@NotNull ExpeditionInformation buildInfo);*/

    /**
     * Returns whether the player is in an expedition currently
     * @param player the player to check
     * @return whether they are in an expedition
     */
    boolean inExpedition(@NotNull Player player);

    /**
     * grabs the player's expedition
     * @param player the player to grab the expedition for
     * @return the expedition the player is in
     */
    Expedition getExpedition(@NotNull Player player);

    /**
     * Ends an expedition. Should only be called by an expedition
     * itself
     * @param toEnd the expedition to end
     */
    void endExpedition(@NotNull Expedition toEnd);
}
