package co.tantleffbeef.pluggytesty.expeditions.listeners;

import co.tantleffbeef.pluggytesty.expeditions.PTExpeditionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

public class PTExpeditionManagerListener implements Listener {
    private final PTExpeditionManager expeditionManager;

    public PTExpeditionManagerListener(@NotNull PTExpeditionManager expeditionManager) {
        this.expeditionManager = expeditionManager;
    }

    @EventHandler
    public void onPlayerMove(@NotNull PlayerMoveEvent event) {

    }
}
