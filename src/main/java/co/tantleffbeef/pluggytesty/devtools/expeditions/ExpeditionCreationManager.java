package co.tantleffbeef.pluggytesty.devtools.expeditions;

import co.tantleffbeef.pluggytesty.expeditions.LocationTraverser;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class ExpeditionCreationManager {
    private final LocationTraverser locationTraverser;
    private final World world;
    private final int plotSize;

    public ExpeditionCreationManager(@NotNull World world, int plotSize) {
        this.locationTraverser = new LocationTraverser();
        this.plotSize = plotSize;
        this.world = world;
    }
}
