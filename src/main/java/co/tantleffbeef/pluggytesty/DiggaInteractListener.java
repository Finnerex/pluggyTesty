package co.tantleffbeef.pluggytesty;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;

public class DiggaInteractListener implements Listener {

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        ItemStack blaster = event.getItem();

        if (blaster == null || blaster.getType() != Material.PRISMARINE_SHARD)
            return;

        ItemMeta blasterMeta = blaster.getItemMeta();

        if (blasterMeta == null || blasterMeta.getLore() == null || !(blasterMeta.getLore().get(0).equals(Digga.DIGGA_LORE)))
            return;

        event.setCancelled(true);

        Dig(3)

    }

        private Block Dig(float range, Location location) {

            World world = location.getWorld();

            Entity entity = null;
            RayTraceResult result = world.rayTraceEntities(location, location.getDirection(), range * 10);

            if (result != null)
                entity = result.getHitEntity();

            if (!(entity instanceof Damageable))
                entity = null;

            for (float i = 0.1f; i < range; i += 0.1f) {
                location.add(location.getDirection().multiply(i));
                world.spawnParticle(Particle.SPELL_INSTANT, location, 1);
            }

            return entity;
        }
}

