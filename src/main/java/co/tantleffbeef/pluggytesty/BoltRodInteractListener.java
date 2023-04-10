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
import org.bukkit.util.RayTraceResult;

public class BoltRodInteractListener implements Listener {

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR &&
                event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.BLAZE_ROD)
            return;

        ItemMeta meta = item.getItemMeta();

        if (meta == null || meta.getLore() == null || !meta.getLore().get(0).equals(BoltRod.ROD_LORE))
            return;

        Player player = event.getPlayer();

        if (player.hasCooldown(Material.BLAZE_ROD))
            return;

        Damageable hitEntity = (Damageable) shootBolt(3.5f, player.getEyeLocation());
        if (hitEntity != null)
            hitEntity.damage(2, player);

        player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);

        player.setCooldown(Material.BLAZE_ROD, 5);

    }

    private Entity shootBolt(float range, Location location) {

        location.add(location.getDirection().multiply(2));
        World world = location.getWorld();

        Entity entity = null;
        RayTraceResult result = world.rayTraceEntities(location, location.getDirection(), range * 10);

        if (result != null)
            entity = result.getHitEntity();

        if (!(entity instanceof Damageable))
            entity = null;

        for(float i = 0.1f; i < range; i += 0.1f) {
            location.add(location.getDirection().multiply(i));
            world.spawnParticle(Particle.SPELL_INSTANT, location, 1);
        }

        return entity;
    }
}

//       TRASH CODE BELOW ----->
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
