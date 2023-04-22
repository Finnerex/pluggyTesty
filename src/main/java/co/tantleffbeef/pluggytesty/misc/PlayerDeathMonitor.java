package co.tantleffbeef.pluggytesty.misc;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerDeathMonitor implements Listener {
    @EventHandler/*(priority = EventPriority.MONITOR)*/
    public void onDeath(PlayerDeathEvent death) {
        death.setKeepLevel(true);
        death.setKeepInventory(false);

        Player victim = death.getEntity();
        List<ItemStack> drops = death.getDrops();

        Bukkit.broadcastMessage("Drops: " + drops);

    }
}
