package co.tantleffbeef.pluggytesty.expeditions.loading;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public enum ExpeditionType {
    TEST_EXPEDITION("test_expedition")

    ;

    private final String id;

    ExpeditionType(@NotNull String id) {
        this.id = id;
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
}
