package co.tantleffbeef.pluggytesty.expeditions;

import org.bukkit.Location;

import java.util.function.Consumer;

public interface Expedition {
    void buildExpedition(Location location, Consumer<Expedition> postBuildCallback);
    void start(Party group);
}
