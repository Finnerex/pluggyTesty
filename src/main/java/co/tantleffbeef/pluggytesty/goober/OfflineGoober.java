package co.tantleffbeef.pluggytesty.goober;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface OfflineGoober {
    /**
     * Returns the OfflinePlayer this plugger wraps
     * @return the OfflinePlayer this plugger wraps
     */
    @NotNull OfflinePlayer asOfflinePlayer();

    /**
     * Returns the unique id of this goober (should be
     * the same as goober.asOfflinePlayer().getUniqueId())
     * @return this goober's unique id
     */
    @NotNull UUID getUniqueId();

    /**
     * Returns whether this plugger is online. If they
     * are, it is safe to cast this to (Online)Plugger.
     * However, if this method is ran before a player
     * joins the server, this plugger will not update
     * and become online when they join.
     *
     * @return whether this plugger is online
     */
    boolean isOnline();

    /**
     * Returns what this plugger's level is. Defaults to 0.
     * @return This plugger's level
     */
    int getLevel();

    /**
     * Sets this plugger's level. Probably won't impact their
     * expedition experience if they are mid-expedition.
     * @param level the level to change this plugger to
     */
    void setLevel(int level);

    /**
     * Adds one level to this plugger.
     */
    default void incrementLevel() {
        addLevels(1);
    }

    /**
     * Adds the specified number of levels
     * to this plugger.
     * @param amount the number of levels to add
     */
    void addLevels(int amount);
}
