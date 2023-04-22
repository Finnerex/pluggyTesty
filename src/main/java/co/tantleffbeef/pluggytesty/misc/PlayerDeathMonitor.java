package co.tantleffbeef.pluggytesty.misc;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class PlayerDeathMonitor implements Listener {

    private final Plugin plugin;

    public PlayerDeathMonitor(Plugin plugin) { this.plugin = plugin; }
    @EventHandler/*(priority = EventPriority.MONITOR)*/
    public void onDeath(PlayerDeathEvent event) {
        event.setKeepInventory(true);

        Player victim = event.getEntity();
        World w = victim.getWorld();
        ItemStack[] drops = victim.getInventory().getContents();
//        int c = 0;
//
//        Bukkit.broadcastMessage("Location: " + victim.getLocation());

        for(int i = 9; i < 36; i++) {
            if(drops[i] != null && drops[i].getMaxStackSize() != 1) {
                w.dropItemNaturally(victim.getLocation(), drops[i]);
//                drops[i] = null;
            }

        }

//        plugin.getServer().getScheduler().runTask(plugin, () -> {
//            victim.getInventory().setContents(drops);
//            victim.updateInventory();
//        });

    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        ItemStack[] drops = player.getInventory().getContents();

        for(int i = 9; i < 36; i++) {
            if(drops[i] != null && drops[i].getMaxStackSize() != 1) {
                drops[i] = null;
            }

        }

        player.getInventory().setContents(drops);
        player.updateInventory();

    }



}
