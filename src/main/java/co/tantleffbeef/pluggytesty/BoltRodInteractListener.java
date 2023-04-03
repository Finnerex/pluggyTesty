package co.tantleffbeef.pluggytesty;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.Objects;

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

        if (!meta.getLore().get(0).equals(BoltRod.ROD_LORE))
            return;

        Player player = event.getPlayer();

        if (player.hasCooldown(Material.BLAZE_ROD))
            return;

        fireBolt(1, 0.05f, player);

    }

    private boolean fireBolt(float range, float speed, Player player) { // returns if hit target

        Location location = player.getEyeLocation();
//        Vector direction = location.getDirection().normalize();

        for(float i = 0.5f; i < range; i += 0.1f * speed) {
            location.add(location.getDirection().multiply(i));
            player.spawnParticle(Particle.SPELL_INSTANT, location, 2);
        }

        return false;
    }
}
