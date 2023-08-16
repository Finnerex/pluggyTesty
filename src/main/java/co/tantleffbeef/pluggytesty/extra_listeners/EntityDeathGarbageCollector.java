package co.tantleffbeef.pluggytesty.extra_listeners;

import co.tantleffbeef.pluggytesty.PluggyTesty;
import co.tantleffbeef.pluggytesty.misc.Debug;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityEvent;

public class EntityDeathGarbageCollector implements Listener {

    @EventHandler
    public void onDeath(EntityEvent event) {
        Entity e = event.getEntity();

        if (!e.isDead())
            return;

        PluggyTesty.removeOnDisable.remove(e);
        Debug.log("Removed entity from event: " + e);
    }

}
