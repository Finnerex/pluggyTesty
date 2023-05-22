package co.tantleffbeef.pluggytesty.expeditions;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import java.util.*;

/**
 * Traverses a world starting at 0, 0 and spiraling out.
 * Great for assigning plots
 */
public class LocationTraverser {
    private final Set<Vector2i> usedLocations;
    private final Queue<Vector2i> availableLocations;
    private TraversalDirection direction;
    private final Vector2i currentLocation;

    public LocationTraverser() {
        usedLocations = new HashSet<>();
        availableLocations = new ArrayDeque<>();
        direction = TraversalDirection.UP;
        currentLocation = new Vector2i(0, 0);

        availableLocations.add(new Vector2i(0, 0));
    }

    private enum TraversalDirection {
        UP(new Vector2i(0, 1)),
        RIGHT(new Vector2i(1, 0)),
        DOWN(new Vector2i(0, -1)),
        LEFT(new Vector2i(-1, 0)),

        ;

        public final Vector2i d;

        TraversalDirection(Vector2i d) {
            this.d = d;
        }

        public TraversalDirection clockwiseDirection() {
            return switch (this) {
                case UP -> RIGHT;
                case RIGHT -> DOWN;
                case DOWN -> LEFT;
                case LEFT -> UP;
            };
        }
    }

    /**
     * Finds the next available location and marks it as assigned
     * @return the next available location
     */
    public @NotNull Vector2ic assignNextAvailableLocation() {
        // Check if there's any roomBoundingBoxes that are available
        if (!availableLocations.isEmpty()) {
            final var location = availableLocations.poll();
            usedLocations.add(location);
            return location;
        }

        // If not, create one

        // Spin to the right and see if the plot is free
        final Vector2i newLocation = new Vector2i(currentLocation);
        newLocation.add(direction.clockwiseDirection().d);

        // If it is then give it
        if (!usedLocations.contains(newLocation)) {
            // reserve the new location
            usedLocations.add(newLocation);
            // mark down our direction change
            direction = direction.clockwiseDirection();
            // Mark down what our new latest location is
            currentLocation.set(newLocation);
            // Give the new location back
            return newLocation;
        }

        // If not then just keep going straight
        newLocation.set(currentLocation);
        newLocation.add(direction.d);
        // This plot should be clear so if not something
        // is really wrong
        assert !usedLocations.contains(newLocation);
        // add the new Location to the list
        usedLocations.add(newLocation);
        // Mark down the new location as our current one
        currentLocation.set(newLocation);
        // Give it back
        return newLocation;
    }

    /**
     * Unmarks the location as assigned
     * @param location the location to "return"
     */
    public void returnLocation(@NotNull Vector2ic location) {
        assert location instanceof Vector2i;
        assert usedLocations.contains(location);

        // Add it to the set of available roomBoundingBoxes
        availableLocations.add((Vector2i) location);
    }
}
