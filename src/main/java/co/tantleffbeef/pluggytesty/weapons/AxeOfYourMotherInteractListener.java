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

import java.util.ArrayList;

public class AxeOfYourMotherInteractListener implements Listener {
    private final Plugin plugin;

    public AxeOfYourMotherInteractListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getAction() != Action.RIGHT_CLICK_AIR &&
                event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.DIAMOND_AXE)
            return;

        ItemMeta meta = item.getItemMeta();

        if (meta == null || meta.getLore() == null || !meta.getLore().get(0).equals(AxeOfYourMother.AOYM_LORE))
            return;

        Player player = event.getPlayer();

        if (player.hasCooldown(Material.DIAMOND_AXE))
            return;

        player.playSound(player, Sound.ENTITY_BAT_TAKEOFF, 1, 1);

        player.setCooldown(Material.DIAMOND_AXE, 300);

        Dash(player);

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                Location location = player.getLocation().add(new Vector(0, -1, 0)).clone();
                if (!location.getBlock().getType().equals(Material.AIR)) {
                    AOE(player);
                    cancel();
                }
            }
        };
        runnable.runTaskTimer(plugin, 10, 0);

    }

    private void Dash(Player player) {
        Location location = player.getLocation().clone();
        location.setPitch(-90);

        player.setVelocity(location.getDirection().normalize().multiply(2).add(player.getVelocity()));
    }

    private void AOE(Player player) {
        Location location = player.getLocation();
        ArrayList<Entity> entities = (ArrayList<Entity>) location.getWorld().getNearbyEntities(location, 2.5, 2, 2.5);

        for (Entity entity : entities) {
            if (entity instanceof Damageable damageable && !entity.equals(player))
                damageable.damage(7);

        }

            location.setX(location.getX() + 2);
            location.setZ(location.getZ() + 2);
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    location.getWorld().spawnParticle(Particle.CLOUD, location, 5/*, location.getBlock().getType().createBlockData()*/);
                    location.setX(location.getX() - 1);
                    Bukkit.broadcastMessage("particle" + location.toString());
                }
                location.setY(location.getY() - 1);
            }
        }
    }