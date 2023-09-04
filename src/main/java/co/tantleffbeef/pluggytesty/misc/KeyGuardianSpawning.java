package co.tantleffbeef.pluggytesty.misc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;


public class KeyGuardianSpawning implements Listener {
    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event) {
        if(event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) return;


    }

}
