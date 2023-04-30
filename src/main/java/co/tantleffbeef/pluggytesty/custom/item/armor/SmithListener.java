package co.tantleffbeef.pluggytesty.custom.item.armor;

import co.tantleffbeef.pluggytesty.armor.BaseArmor;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;

import static org.bukkit.Bukkit.getRecipe;


public class SmithListener implements Listener {

    NamespacedKey key = NamespacedKey.minecraft("netherite_chestplate");

    @EventHandler
    public void onCraft(PrepareSmithingEvent event) {
        final SmithingInventory inventory = event.getInventory();
        final ItemStack result = inventory.getResult();

        if (result.equals(getRecipe(key).getResult())) {
            event.getInventory().setResult(BaseArmor.nC());
        }
    }
}


