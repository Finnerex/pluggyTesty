package co.tantleffbeef.pluggytesty;

import org.bukkit.*;
import org.bukkit.block.Block;
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
import org.bukkit.util.RayTraceResult;

public class LauncherInteractListener implements Listener {

    private final Plugin plugin;

    public LauncherInteractListener(Plugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR &&
                event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.NETHER_BRICK)
            return;

        ItemMeta meta = item.getItemMeta();

        if (meta == null || meta.getLore() == null || !meta.getLore().get(0).equals(Launcher.LAUNCH_LORE))
            return;

        Player player = event.getPlayer();

        if (player.hasCooldown(Material.NETHER_BRICK))
            return;

//        Damageable hitEntity = (Damageable) shootBolt(3.5f, player.getEyeLocation());
//        if (hitEntity != null)
//            hitEntity.damage(2, player);

        BukkitRunnable runnable = new BukkitRunnable() {
            Location location = player.getLocation();
            int runs = 0;

            @Override
            public void run() {
                Block block = shootBolt(1, location);
                player.getWorld().spawnParticle(Particle.SPELL, location, 1);

                if (block != null || runs > 10) {
                    if (block != null)
                        location = block.getLocation();

                    player.getWorld().createExplosion(location, 3);
                    cancel();
                    return;
                }

                float p = location.getPitch();
                location.setPitch(p + 5 + ((p + 90) / -36));
                runs ++;
            }
        };

        runnable.runTaskTimer(plugin, 0, 1);

        //player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);

        player.setCooldown(Material.NETHER_BRICK, 5);
    }

    private Block shootBolt(float range, Location location) {

        World world = location.getWorld();

        Block block = null;
        RayTraceResult result = world.rayTraceBlocks(location, location.getDirection(), range);

        if (result != null)
            block = result.getHitBlock();

        for(float i = 0.1f; i < range; i += 0.1f) {
            location.add(location.getDirection().normalize().multiply(i));
            world.spawnParticle(Particle.SPELL, location, 1);
        }

        return block;
    }


}
