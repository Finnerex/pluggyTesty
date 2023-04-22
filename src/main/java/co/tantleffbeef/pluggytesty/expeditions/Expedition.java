package co.tantleffbeef.pluggytesty.expeditions;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class Expedition {
    private final ExpeditionType type;
    private final Location location;
    private Room[] rooms;

    /**
     * Creates an expedition object with this type
     * @param type the type of the expedition
     * @param location where to build it at
     */
    public Expedition(ExpeditionType type, Location location) {
        this.type = type;
        this.location = location;
    }

    /**
     * Builds the expedition at this objects location, and once completed
     * calls the callback
     * @param postBuildCallback the callback to call once the expedition
     *                          has been created in the world. generally
     *                          used for starting an expedition or
     *                          telling the player that it is ready to
     *                          start.
     */
    public void buildExpedition(Consumer<Expedition> postBuildCallback) {
        type.build(location, (rooms) -> {
            this.rooms = rooms;
            postBuildCallback.accept(this);
        });
    }

    /**
     * Starts the expedition with this group of players
     * Precondition: expedition already built
     * @param group the players who will join
     *              the expedition
     */
    public void start(@NotNull Party group) {
        assert rooms != null;


    }
}
