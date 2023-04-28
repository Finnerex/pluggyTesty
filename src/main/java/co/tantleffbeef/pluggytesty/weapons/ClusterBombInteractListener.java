package co.tantleffbeef.pluggytesty.weapons;

import co.tantleffbeef.pluggytesty.PluggyTesty;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ClusterBombInteractListener implements Listener {
    private final Plugin plugin;

    public ClusterBombInteractListener(Plugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getAction() != Action.RIGHT_CLICK_AIR &&
                event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.COAL_BLOCK)
            return;

        ItemMeta meta = item.getItemMeta();

        if (meta == null || meta.getLore() == null || !meta.getLore().get(0).equals(ClusterBomb.CB_LORE))
            return;

        event.setCancelled(true);
        Player player = event.getPlayer();

        if (player.hasCooldown(Material.COAL_BLOCK))
            return;

        player.playSound(player, Sound.ENTITY_ENDER_PEARL_THROW, 1, 1);

        player.setCooldown(Material.COAL_BLOCK, 10);

        Location location = player.getEyeLocation();

        EnderPearl pearl = player.getWorld().spawn(location, EnderPearl.class, (enderPearl) -> {
            enderPearl.setGravity(false);
            enderPearl.setVelocity(location.getDirection().normalize());
        });

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> explode(pearl), 40);

    }
    private void explode(EnderPearl pearl) {
        Location location = pearl.getLocation();


        pearl.remove();

        for (int i = 0; i < 8; i++) {
            location.getWorld().spawn(location, Arrow.class, (arrow) -> arrow.setVelocity(location.getDirection().normalize()));
            location.setYaw(location.getYaw() + 45);
        }

    }

}
