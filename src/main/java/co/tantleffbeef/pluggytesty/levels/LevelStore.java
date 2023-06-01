package co.tantleffbeef.pluggytesty.levels;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface LevelStore {
    int retrieveLevel(@NotNull UUID player);
    void storeLevel(@NotNull UUID player, int level);

}
