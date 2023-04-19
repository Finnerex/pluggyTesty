package co.tantleffbeef.pluggytesty;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class GoerInteractListener implements Listener {

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR &&
                event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.LEATHER)
            return;

        ItemMeta meta = item.getItemMeta();

        if (meta == null || meta.getLore() == null || !meta.getLore().get(0).equals(Goer.GOER_LORE))
            return;

        Player player = event.getPlayer();

        if (player.hasCooldown(Material.LEATHER))
            return;

        Vector direction = player.getEyeLocation().getDirection();
        player.setVelocity(direction.multiply(2).add(player.getVelocity()));

        player.setCooldown(Material.LEATHER, 0);

    }

}
