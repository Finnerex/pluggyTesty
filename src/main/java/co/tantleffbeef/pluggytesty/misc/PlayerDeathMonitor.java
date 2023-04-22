package co.tantleffbeef.pluggytesty.misc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;


public class PlayerDeathMonitor implements Listener {

    @EventHandler/*(priority = EventPriority.MONITOR)*/
    public void onDeath(PlayerDeathEvent event) {
        if (!event.getKeepInventory())
            return;

        Player victim = event.getEntity();
        ItemStack[] drops = victim.getInventory().getContents();

        for(int i = 9; i < 36; i++) {
            if(drops[i] != null && drops[i].getMaxStackSize() != 1)
                victim.getWorld().dropItemNaturally(victim.getLocation(), drops[i]);

        }

    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        ItemStack[] drops = player.getInventory().getContents();

        for(int i = 9; i < 36; i++) {
            if(drops[i] != null && drops[i].getMaxStackSize() != 1)
                drops[i] = null;

        }

        player.getInventory().setContents(drops);
        player.updateInventory();

    }



}
