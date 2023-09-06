package co.tantleffbeef.pluggytesty.expeditions.listeners;

import co.tantleffbeef.pluggytesty.expeditions.PTExpeditionController;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PTExpeditionManagerListener implements Listener {
    private final PTExpeditionController expeditionManager;

    public PTExpeditionManagerListener(@NotNull PTExpeditionController expeditionManager) {
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

    @EventHandler
    public void onPlayerDamageEvent(@NotNull EntityDamageEvent event) {
        final var entity = event.getEntity();
        if (!(entity instanceof Player player))
            return;

        Bukkit.broadcastMessage("on player damage by block");

        // Check if this event is even relevant
        if (!expeditionManager.inExpedition(player))
            return;

        expeditionManager.onPlayerDamage(player, event);
    }
}
