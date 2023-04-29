package co.tantleffbeef.pluggytesty.utility;

import co.tantleffbeef.pluggytesty.utility.Dash;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class DashInteractListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.FEATHER)
            return;

        ItemMeta meta = item.getItemMeta();

        if (meta == null || meta.getLore() == null || !meta.getLore().get(0).equals(Dash.DASH_LORE))
            return;

        Player player = event.getPlayer();

        if (player.hasCooldown(Material.FEATHER))
            return;

        player.playSound(player, Sound.ENTITY_EGG_THROW, 1, 1);

        Dash(player);

        player.setCooldown(Material.FEATHER, 80);
    }

    private void Dash(Player player) {
        Vector direction = player.getEyeLocation().getDirection();

        player.setVelocity(direction.normalize().multiply(2).add(player.getVelocity()));
    }
}