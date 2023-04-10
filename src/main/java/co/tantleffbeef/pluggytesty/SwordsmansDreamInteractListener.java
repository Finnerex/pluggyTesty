package co.tantleffbeef.pluggytesty;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.Random;

public class SwordsmansDreamInteractListener implements Listener {

    private final Plugin plugin;

    public SwordsmansDreamInteractListener(Plugin plugin) { this.plugin = plugin; }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.IRON_SWORD) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || meta.getLore() == null || !meta.getLore().get(0).equals(SwordsmansDream.SWORD_LORE)) return;

        Player player = event.getPlayer();
        if (player.hasCooldown(Material.IRON_SWORD)) return;

        World world = player.getWorld();

        if(player.isSneaking()) {
            player.setGravity(false);
            player.setVelocity(new Vector(0, 0.2, 0));
            // wait
            BukkitRunnable runnable = new BukkitRunnable() {
                int tickNum = 0;
                @Override
                public void run() {
                    tickNum ++;
                    world.spawnParticle(Particle.SWEEP_ATTACK, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 5, 1.0, 0.0, 1.0);

                    if (tickNum >= 20) player.setVelocity(new Vector(0, 0,0));
                    if(tickNum >= 30) {
                        cancel();
                        return;
                    }
                }
            };

            runnable.runTaskTimer(plugin, 0, 1);
            // dash down
            // recharge
        } else {
            return;
        }
        player.setCooldown(Material.IRON_SWORD, 5);
        player.playSound(player, Sound.BLOCK_GRINDSTONE_USE, 1, 1);

        final float range = 2f;


    }
}
