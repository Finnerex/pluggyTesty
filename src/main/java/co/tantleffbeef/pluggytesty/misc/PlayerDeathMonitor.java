package co.tantleffbeef.pluggytesty.misc;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class PlayerDeathMonitor implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (event.getFinalDamage() < player.getHealthScale())
            return;

        ItemStack[] drops = player.getInventory().getExtraContents();
        int c = 0;

        Bukkit.broadcastMessage("Location: " + victim.getLocation());

        for(ItemStack d : drops) {
            c++;
            // check if item is what we want to keep.
            // If it isn't, plonk it on the floor
            Bukkit.broadcastMessage("drop #" + c + ": " + d);
        }


    }
}
