package co.tantleffbeef.pluggytesty.expeditions.loading;

import co.tantleffbeef.pluggytesty.expeditions.Expedition;
import co.tantleffbeef.pluggytesty.expeditions.ExpeditionManager;
import co.tantleffbeef.pluggytesty.expeditions.RoomMetadata;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ExpeditionConstructor {
    @NotNull Expedition construct(@NotNull ExpeditionManager manager, @NotNull Location minimumCorner, @NotNull RoomMetadata[] rooms);
}
