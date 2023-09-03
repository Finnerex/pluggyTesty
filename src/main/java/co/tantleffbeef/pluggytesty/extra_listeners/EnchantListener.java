package co.tantleffbeef.pluggytesty.extra_listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

public class EnchantListener implements Listener {
    @EventHandler
    public void onOpenEnchanter(InventoryOpenEvent event) {
        if(event.getInventory().getType().equals(InventoryType.ENCHANTING) && !event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            event.setCancelled(true);
        }
    }
}
