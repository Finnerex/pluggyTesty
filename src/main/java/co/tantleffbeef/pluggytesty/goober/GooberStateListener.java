package co.tantleffbeef.pluggytesty.goober;

import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class GooberStateListener implements Listener {
    private final GooberStateController controller;
    private final Server server;

    public GooberStateListener(@NotNull GooberStateController controller,
                               @NotNull Server server) {
        this.controller = controller;
        this.server = server;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        controller.onPlayerJoin(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLeave(@NotNull PlayerQuitEvent event) {
        final var player = server.getOfflinePlayer(event.getPlayer().getUniqueId());

        controller.onPlayerLeave(player);
    }
}
