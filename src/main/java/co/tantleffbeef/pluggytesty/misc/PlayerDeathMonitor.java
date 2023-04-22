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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class PlayerDeathMonitor implements Listener {

    private final Plugin plugin;

    public PlayerDeathMonitor(Plugin plugin) { this.plugin = plugin; }
    @EventHandler/*(priority = EventPriority.MONITOR)*/
    public void onDeath(PlayerDeathEvent event) {

        Player victim = event.getEntity();
        World w = victim.getWorld();
        ItemStack[] drops = victim.getInventory().getContents();
        int c = 0;

        Bukkit.broadcastMessage("Location: " + victim.getLocation());

        for(int i = 9; i < 36; i++) {
            if(drops[i].getMaxStackSize() != 1) {
                w.dropItemNaturally(victim.getLocation(), drops[i]);
                drops[i] = null;
            }

        }


        plugin.getServer().getScheduler().runTask(plugin, () -> {
            victim.getInventory().setContents(drops);
            victim.updateInventory();
        });


    }

//    public void onDamage(EntityDamageEvent event) {
//        if (!(event.getEntity() instanceof Player player)) return;
//
//        if (event.getFinalDamage() < player.getHealthScale())
//            return;
//
//        ItemStack[] drops = player.getInventory().getContents();
//        int c = 0;
//
//        Bukkit.broadcastMessage("Location: " + player.getLocation());
//
//        for(ItemStack d : drops) {
//            c++;
//            // check if item is what we want to keep.
//            // If it isn't, plonk it on the floor
//            Bukkit.broadcastMessage("drop #" + c + ": " + d);
//        }
}
