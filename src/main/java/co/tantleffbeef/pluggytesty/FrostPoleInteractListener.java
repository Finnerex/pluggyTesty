package co.tantleffbeef.pluggytesty;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
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

public class FrostPoleInteractListener {
    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.STICK) return;

        ItemMeta meta = item.getItemMeta();
        if (!meta.getLore().equals(MagicStick.STICK_LORE)) return;

        Player player = event.getPlayer();
        if (player.hasCooldown(Material.SOUL_TORCH)) return;

        final float range = 4f;
        Entity target = iceBolt(range, player);
        target.setFreezeTicks(220);

    }

    private Entity iceBolt(float r, Player p) {
        Location location = p.getEyeLocation();
        Entity entity = p.getWorld().rayTraceEntities(location, location.getDirection(), r * 10).getHitEntity();

        for(float i = 0.5f; i < r; i += 0.1f) {
            location.add(location.getDirection().multiply(i));
            p.spawnParticle(Particle.SPELL_INSTANT, location, 2);
        }
        return entity;
    }
}
