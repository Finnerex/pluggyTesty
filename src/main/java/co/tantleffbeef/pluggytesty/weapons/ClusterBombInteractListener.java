package co.tantleffbeef.pluggytesty.weapons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ClusterBombInteractListener implements Listener {
    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR &&
                event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.ENDER_PEARL)
            return;

        ItemMeta meta = item.getItemMeta();

        if (meta == null || meta.getLore() == null || !meta.getLore().get(0).equals(ClusterBomb.CB_LORE))
            return;

        event.setCancelled(true);
        Player player = event.getPlayer();

        if (player.hasCooldown(Material.BLAZE_ROD))
            return;


    }
}
