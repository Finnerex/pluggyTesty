package co.tantleffbeef.pluggytesty.expeditions.loading;

import co.tantleffbeef.pluggytesty.expeditions.Expedition;
import co.tantleffbeef.pluggytesty.expeditions.ExpeditionController;
import co.tantleffbeef.pluggytesty.expeditions.RoomMetadata;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ExpeditionConstructor {
    @NotNull Expedition construct(@NotNull ExpeditionController controller, @NotNull Location minimumCorner, @NotNull RoomMetadata[] rooms);
}
