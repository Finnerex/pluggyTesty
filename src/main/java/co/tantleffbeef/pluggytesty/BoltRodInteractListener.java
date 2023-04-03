package co.tantleffbeef.pluggytesty;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;

public class BoltRodInteractListener implements Listener {
    private final PluggyTesty plugin;

    public BoltRodInteractListener(PluggyTesty plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR &&
                event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.BLAZE_ROD)
            return;

        ItemMeta meta = item.getItemMeta();

        if (!meta.getLore().get(0).equals(BoltRod.ROD_LORE))
            return;

        Player player = event.getPlayer();

        if (player.hasCooldown(Material.BLAZE_ROD))
            return;

        fireBolt(20, player);

    }

    private boolean fireBolt(float range, Player player) { // returns if hit target

        Location location = player.getEyeLocation();

        final float start = 0.5f;
        BukkitRunnable runnable = new BukkitRunnable() {
            private float i = start;

            @Override
            public void run() {
                i += 1f;
                if (i >= range) {
                    cancel();
                    return;
                }

                Location currentLocation = location.clone();

                for (float j = start; j < i; j += 0.25f) {
                    currentLocation.add(location.getDirection().multiply(j));
                    player.spawnParticle(Particle.ELECTRIC_SPARK, location, 2);
                }
            }
        };

        runnable.runTaskTimer(plugin, 0, 0);

//        for(float i = 0.5f; i < range; i += 0.1f) {
//            location.add(location.getDirection().multiply(i));
//            player.spawnParticle(Particle.SPELL_INSTANT, location, 2);
//        }

        return false;
    }
}
