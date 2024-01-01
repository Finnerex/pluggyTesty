package co.tantleffbeef.pluggytesty.levels.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import co.tantleffbeef.pluggytesty.goober.OfflineGoober;

public class GooberLevelChangeEvent extends Event implements Cancellable {
    private static HandlerList handlerList = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    private final OfflineGoober goober;
    private final int previousLevel;
    private final int newLevel;
    private boolean cancelled = false;

    public GooberLevelChangeEvent(OfflineGoober goober, int previousLevel, int newLevel) {
        this.goober = goober;
        this.previousLevel = previousLevel;
        this.newLevel = newLevel;
    }

    public OfflineGoober getGoober() {
        return goober;
    }

    public int getPreviousLevel() {
        return previousLevel;
    }

    public int getNewLevel() {
        return newLevel;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean isCancelled) {
        cancelled = isCancelled;
    }
}
