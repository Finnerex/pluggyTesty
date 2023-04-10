package co.tantleffbeef.pluggytesty;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
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

import java.util.List;

public class WorldEnderInteractListener implements Listener {

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        ItemStack blaster = event.getItem();

        if (blaster == null || blaster.getType() != Material.END_ROD)
            return;

        ItemMeta blasterMeta = blaster.getItemMeta();

        if (blasterMeta == null || blasterMeta.getLore() == null || !(blasterMeta.getLore().get(0).equals(WorldEnder.ENDER_LORE)))
            return;

        event.setCancelled(true);

        Player player = event.getPlayer();

        for (int i = 0; i < 3; i++){
            Location l = player.getEyeLocation();
            Location location = new Location(player.getWorld(), l.getX() - 1, l.getY() - 1, l.getZ());
            location.setY(l.getY() + i);
            for (int j = 0; j < 3; j++){
                location.setX(l.getX() + j);
                Damageable hitEntity = (Damageable) shootArea(3.5f, location);
                if (hitEntity != null)
                    hitEntity.damage(2, player);
            }
        }
    }

        private Entity shootArea(float range, Location location) {

            location.add(location.getDirection().multiply(2));
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

