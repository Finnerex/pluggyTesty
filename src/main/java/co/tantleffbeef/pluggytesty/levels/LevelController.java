package co.tantleffbeef.pluggytesty.levels;

import org.bukkit.OfflinePlayer;

public interface LevelController {
    /**
     * Gives back the level of the player
     * @param player player to check the level of
     * @return the level of player
     */
    int getPlayerLevel(OfflinePlayer player);

    /**
     * Levels up the player by 1
     * @param player the player to level up
     */
    default void levelUp(OfflinePlayer player) {
        addLevels(player, 1);
    }
    void addLevels(OfflinePlayer player, int amount);

    /**
     * Sets the players level - should be used for like
     * an admin command or something
     * @param player the player to set the level of
     * @param level the level to set the player to
     */
    void setLevel(OfflinePlayer player, int level);
}
