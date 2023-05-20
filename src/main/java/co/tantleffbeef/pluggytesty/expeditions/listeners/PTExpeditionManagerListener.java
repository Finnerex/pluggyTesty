package co.tantleffbeef.pluggytesty.expeditions.listeners;

import co.tantleffbeef.pluggytesty.PluggyTesty;
import co.tantleffbeef.pluggytesty.expeditions.PTExpeditionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PTExpeditionManagerListener implements Listener {
    private final PTExpeditionManager expeditionManager;

    public PTExpeditionManagerListener(@NotNull PTExpeditionManager expeditionManager) {
        this.expeditionManager = expeditionManager;
    }

    @EventHandler
    public void onPlayerMove(@NotNull PlayerMoveEvent event) {
        final var player = event.getPlayer();
        // Check if this event is even relevant
        if (!expeditionManager.inExpedition(player))
            return;

        if (event.getTo() == null)
            return;

        // Cancel the event if they're trying to leave the expedition world while still being in an expedition
        if (!Objects.equals(event.getFrom().getWorld(), event.getTo().getWorld()))
            event.setCancelled(true);

        expeditionManager.onPlayerMoved(player, event.getTo());
    }
}
