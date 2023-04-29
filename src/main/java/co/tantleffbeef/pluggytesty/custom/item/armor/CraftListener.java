package co.tantleffbeef.pluggytesty.custom.item.armor;

import co.tantleffbeef.pluggytesty.armor.BaseArmor;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.CraftingInventory;

import static org.bukkit.Bukkit.getRecipe;


public class CraftListener implements Listener {

    NamespacedKey key = NamespacedKey.minecraft("diamond_chestplate");

    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
        final CraftingInventory inventory = event.getInventory();

        if (inventory.getResult().equals(getRecipe(key).getResult())) {
            event.getInventory().setResult(BaseArmor.dC());
        }
    }
}

