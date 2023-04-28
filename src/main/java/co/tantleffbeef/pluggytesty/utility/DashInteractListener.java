package co.tantleffbeef.pluggytesty.utility;

import co.tantleffbeef.pluggytesty.utility.Dash;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class DashInteractListener implements Listener {
    private Plugin plugin;

    public DashInteractListener(Plugin plugin) {
        this.plugin = plugin;
    }
     private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.FEATHER)
            return;

        ItemMeta meta = item.getItemMeta();

        if (meta == null || ((ItemMeta) meta).getLore() == null || !meta.getLore().get(0).equals(Dash.DASH_LORE))
            return;

        event.setCancelled(true);

        Player player = event.getPlayer();

        if (player.hasCooldown(Material.FEATHER))
            return;
        player.playSound(player, Sound.ENTITY_EGG_THROW, 1, 1);

        player.setCooldown(Material.FEATHER, 80);

        Dash(player);
     }
    private void Dash(Player player) {
        Location location = player.getEyeLocation();

        player.setVelocity(location.getDirection().normalize().multiply(2));
    }
    }