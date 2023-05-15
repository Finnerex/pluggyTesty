package co.tantleffbeef.pluggytesty.expeditions;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.math.BlockVector3Imp;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.Consumer;

public class TestExpedition implements Expedition {
    private final Plugin schedulerPlugin;
    private Location builtLocation;
    private Location startLocation;
    private boolean built;

    public TestExpedition(@NotNull Plugin schedulerPlugin) {
        this.schedulerPlugin = schedulerPlugin;
        built = false;
    }

    @Override
    public void build(@NotNull BukkitScheduler scheduler, @NotNull Location location, @NotNull Consumer<Room[]> postBuildCallback) {
        assert location.getWorld() != null;

        // Paste the schematic async
        scheduler.runTaskAsynchronously(schedulerPlugin, () -> {
            // Load the schematic using FaweAPI
            try (final var schematic = FaweAPI.load(schedulerPlugin.getDataFolder().toPath().resolve("bruh.schematic").toFile())) {
                // Paste it once its loaded
                schematic.paste(FaweAPI.getWorld(location.getWorld().getName()), BlockVector3Imp.at(location.getBlockX(), location.getBlockY(), location.getBlockZ()));

                // After done pasting, run the callback on the main thread
                scheduler.runTask(schedulerPlugin, () -> postBuildCallback.accept(null));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void start(@NotNull Party party) {

    }

    public void delete() {

    }

    private void setStartLocation() {
        assert built;
    }
}
