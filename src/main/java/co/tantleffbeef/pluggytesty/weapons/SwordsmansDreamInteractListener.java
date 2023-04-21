package co.tantleffbeef.pluggytesty.weapons;

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
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

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
            player.setCooldown(Material.IRON_SWORD, 60);
            player.playSound(player, Sound.BLOCK_GRINDSTONE_USE, 1, 1);
            player.setGravity(false);
            player.setVelocity(new Vector(0, 0.2, 0));
            // Attribute damage = item.getItemMeta().getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE);
            BukkitRunnable runnable = new BukkitRunnable() {
                int tickNum = 0;
                @Override
                public void run() { // hi
                    tickNum ++;
                    world.spawnParticle(Particle.SWEEP_ATTACK, player.getLocation(), 3, 1.0, 0.0, 1.0);
                    for(Entity i : player.getNearbyEntities(2, 2, 2)) {

                        if (i instanceof Damageable d)
                            d.damage(6, player);
                    }
                    if(tickNum >= 20) {
                        player.setVelocity(new Vector(0, 0,0));
                    }
                    if(tickNum >= 30 && tickNum < 50) {
                        world.spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 1);
                        player.setVelocity(player.getLocation().getDirection());
                    }
                    if(tickNum >= 50) {
                        cancel();
                        player.setGravity(true);
                        player.setVelocity(player.getLocation().getDirection());

                    }
                }
            };

            runnable.runTaskTimer(plugin, 0, 1);

        } else {
            return;
        }

        final float range = 2f;


    }
}
