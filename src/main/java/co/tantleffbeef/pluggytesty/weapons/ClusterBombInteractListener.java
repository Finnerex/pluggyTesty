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
        Bukkit.broadcastMessage("event");
        if (event.getAction() != Action.RIGHT_CLICK_AIR &&
                event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        ItemStack item = event.getItem();
        Bukkit.broadcastMessage("Right-Click");
        if (item == null || item.getType() != Material.COAL_BLOCK)
            return;

        ItemMeta meta = item.getItemMeta();
        Bukkit.broadcastMessage("Coal Block");
        if (meta == null || meta.getLore() == null || !meta.getLore().get(0).equals(ClusterBomb.CB_LORE))
            return;
        Bukkit.broadcastMessage("Meta");
        event.setCancelled(true);
        Player player = event.getPlayer();
        Bukkit.broadcastMessage("setCancelled");
        if (player.hasCooldown(Material.COAL_BLOCK))
            return;
        Bukkit.broadcastMessage("Cooldown");
        player.playSound(player, Sound.ENTITY_ENDER_PEARL_THROW, 1, 1);

        player.setCooldown(Material.COAL_BLOCK, 10);

        Location location = player.getEyeLocation();

        Snowball pearl = (Snowball) player.getWorld().spawnEntity(location, EntityType.SNOWBALL);
        pearl.setGravity(false);
        pearl.setVelocity(location.getDirection().normalize());

        Bukkit.broadcastMessage("RTL");
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> explode(pearl), 40);

    }
    private void explode(Snowball pearl) {
        Location location = pearl.getLocation();


        pearl.remove();

        for (int i = 0; i < 8; i++) {
            Arrow arrow = (Arrow) location.getWorld().spawnEntity(location, EntityType.ARROW);
            arrow.setVelocity(location.getDirection().normalize());
            location.setYaw(location.getYaw() + 45);
        }

    }

}
