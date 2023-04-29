package co.tantleffbeef.pluggytesty.custom.item.armor;

import co.tantleffbeef.pluggytesty.PluggyTesty;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;

import static org.bukkit.Bukkit.getRecipe;


public class CraftListener implements Listener {

    NamespacedKey key = NamespacedKey.minecraft("Leather_Helmet");

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        Bukkit.broadcastMessage("Craft event");

        if (event.getRecipe().equals(getRecipe(key))) {
            event.getInventory().setResult(PluggyTesty.leatherHelmet());
        }
    }
}

