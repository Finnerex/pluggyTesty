package co.tantleffbeef.pluggytesty.bosses;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ExplodeListener implements Listener {

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        Bukkit.broadcastMessage("Entity: " + event.getEntity());
    }

}
