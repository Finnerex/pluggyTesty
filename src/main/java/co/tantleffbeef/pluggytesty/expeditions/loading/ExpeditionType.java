package co.tantleffbeef.pluggytesty.expeditions.loading;

import co.tantleffbeef.pluggytesty.expeditions.TestExpedition;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public enum ExpeditionType {
    TEST_EXPEDITION("default", TestExpedition::new)

    ;

    private final String id;
    private final ExpeditionConstructor constructor;

    ExpeditionType(@NotNull String id, @NotNull ExpeditionConstructor constructor) {
        this.id = id;
        this.constructor = constructor;
    }

    /**
     * Tries to grab the room with the id passed in. Has O(n) time complexity but
     * shouldn't run that often so who cares
     * @param searchId the id to look for
     * @return either the RoomType or Optional.empty()
     */
    public static Optional<ExpeditionType> getExpeditionWithId(@NotNull String searchId) {
        // Loop through every room type and see if
        // any match the search id
        for (var type : values()) {
            // check if this is the type they're looking for
            if (type.id.equals(searchId))
                return Optional.of(type);
        }

        return Optional.empty();
    }

    public @NotNull ExpeditionConstructor getConstructor() {
        return constructor;
    }
}
