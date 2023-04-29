package co.tantleffbeef.pluggytesty.custom.item.armor;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.w3c.dom.events.Event;

import static org.bukkit.Bukkit.getRecipe;


public class ArmorCraft implements Listener {

    NamespacedKey key = NamespacedKey.minecraft("Leather_Helmet");

    @EventHandler
    public void Craft(PrepareItemCraftEvent event) {

        if (event.getRecipe().equals(getRecipe(key))) {
            CraftItemEvent Item = new CraftItemEvent(getRecipe(key), event.getView(), InventoryType.SlotType.RESULT, 0, ClickType.LEFT, InventoryAction.PICKUP_ALL);
        }
    }
}

