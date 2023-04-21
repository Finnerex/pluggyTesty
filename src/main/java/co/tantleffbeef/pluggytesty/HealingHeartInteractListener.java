package co.tantleffbeef.pluggytesty;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class HealingHeartInteractListener implements Listener {
    private final Plugin plugin;

    public HealingHeartInteractListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.REDSTONE)
            return;

        ItemMeta meta = item.getItemMeta();

        if (meta == null || meta.getLore() == null || !meta.getLore().get(0).equals(HealingHeart.HEART_LORE))
            return;

        event.setCancelled(true);

        Player player = event.getPlayer();

        if (player.hasCooldown(Material.REDSTONE))
            return;

        int amount = item.getAmount();
        // make runnable at beginning, if charge has not incremented in x ticks, heal and reset

        if(amount == 1) {
            BukkitRunnable runnable = new BukkitRunnable() {
                private int tickNum = 0;
                private int lastAmount = 1;

                @Override
                public void run() {
                    tickNum ++;
                    int curAmount = item.getAmount();

                    if(curAmount != lastAmount)
                        tickNum = 0;

                    if (tickNum > 15) { // last and current amount are the same for 15 ticks
                        heal(player, item);
                        cancel();
                        return;
                    }

                    lastAmount = curAmount;

                }

            };

            runnable.runTaskTimer(plugin, 0, 0);

        }

        item.setAmount(Math.min(amount + 1, 10)); //every tick(s) it is held for, max 60
        player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1 + item.getAmount() * 0.01f);

    }

    // when the player releases right click
    private void heal(Player player, ItemStack item) {
        player.playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        player.setHealth(Math.min(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), (player.getHealth() + item.getAmount())));
        player.setCooldown(Material.REDSTONE, 60);

        item.setAmount(1);
    }
}
