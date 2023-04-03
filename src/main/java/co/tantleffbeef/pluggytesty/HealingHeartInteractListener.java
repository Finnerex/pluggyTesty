package co.tantleffbeef.pluggytesty;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HealingHeartInteractListener implements Listener {
    private static int charge = 0;
    private static long lastTime = 0;
    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.REDSTONE)
            return;

        ItemMeta meta = item.getItemMeta();

        if (!meta.getLore().get(0).equals(HealingHeart.HEART_LORE))
            return;

        event.setCancelled(true);

        Player player = event.getPlayer();

        if (player.hasCooldown(Material.REDSTONE))
            return;

        int timeDif = (int) (System.currentTimeMillis() - lastTime);
        lastTime = System.currentTimeMillis();

        if (timeDif > 250) {
            heal(player);
            charge = 0;
            return;
        }

        player.sendMessage("c: " + charge);

        charge += (charge < 60) ? 1 : 0; //every tick it is held for, max 3 sec

        player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, charge / 20.0f);

    }

    // when the player releases right click
    private void heal(Player player) {
        player.playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        player.damage(-charge / 10.0f);
        //player.setHealth((player.getHealth() + charge / 10.0f ));
        player.setCooldown(Material.REDSTONE, 60);
    }
}
