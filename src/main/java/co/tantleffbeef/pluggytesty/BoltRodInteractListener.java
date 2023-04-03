package co.tantleffbeef.pluggytesty;

import org.bukkit.*;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

        ((Damageable) shootBolt(3.5f, player)).damage(10, (Entity) player);

    }

    private Entity shootBolt(float range, Player player) {

        Location location = player.getEyeLocation();

        Entity entity = player.getWorld().rayTraceEntities(location, location.getDirection(), range * 10).getHitEntity();
        if (entity != null)
            player.sendMessage(entity.toString());
        else
            player.sendMessage("no entity");

        for(float i = 0.5f; i < range; i += 0.1f) {
            location.add(location.getDirection().multiply(i));
            player.spawnParticle(Particle.SPELL_INSTANT, location, 2);
        }

        return entity;
    }
}

//        final float start = 0.5f;
//        BukkitRunnable runnable = new BukkitRunnable() {
//            private float i = start;
//
//            @Override
//            public void run() {
//                i += 1f;
//                if (i >= range) {
//                    cancel();
//                    return;
//                }
//
//                Location currentLocation = location.clone();
//
//                for (float j = start; j < i; j += 0.25f) {
//                    currentLocation.add(location.getDirection().multiply(j));
//                    player.spawnParticle(Particle.SPELL_INSTANT, location, 1);
//                }
//            }
//        };
//
//        runnable.runTaskTimer(plugin, 0, 0);
