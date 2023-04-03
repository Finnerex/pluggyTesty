package co.tantleffbeef.pluggytesty;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.Random;

public class FrostPoleInteractListener implements Listener {
    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.SOUL_TORCH) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.getLore().get(0).equals(FrostPole.POLE_LORE)) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        if (player.hasCooldown(Material.SOUL_TORCH)) return;

        final float range = 4f;
        Entity target = shootBolt(range, player);
        target.setFreezeTicks(220);

    }

    private Entity shootBolt(float r, Player p) {
        Location location = p.getEyeLocation();
        Entity entity = p.getWorld().rayTraceEntities(location, location.getDirection(), r * 10).getHitEntity();

        for(float i = 0.5f; i < r; i += 0.1f) {
            location.add(location.getDirection().multiply(i));
            p.spawnParticle(Particle.SPELL_INSTANT, location, 2);
        }
        return entity;
    }
}
