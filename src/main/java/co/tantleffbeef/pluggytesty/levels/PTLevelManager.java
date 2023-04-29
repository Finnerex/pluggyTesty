package co.tantleffbeef.pluggytesty.levels;

import org.bukkit.OfflinePlayer;

public class PTLevelManager implements LevelManager {
    @Override
    public int getPlayerLevel(OfflinePlayer player) {
        return 1;
    }

    @Override
    public void levelUp(OfflinePlayer player) {
        // TODO
    }

    @Override
    public void setLevel(OfflinePlayer player, int level) {
        // TODO
    }
}
