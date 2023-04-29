package co.tantleffbeef.pluggytesty.custom.item.armor;

import co.tantleffbeef.pluggytesty.BaseArmors.BaseArmor;
import co.tantleffbeef.pluggytesty.PluggyTesty;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.CraftingInventory;

import static org.bukkit.Bukkit.getRecipe;


public class CraftListener implements Listener {

    NamespacedKey key = NamespacedKey.minecraft("netherite_chestplate");

    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
        final CraftingInventory inventory = event.getInventory();

        if (inventory.getResult().equals(getRecipe(key).getResult())) {
            event.getInventory().setResult(BaseArmor.nC());
        }
    }
}

