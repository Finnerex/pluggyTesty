package co.tantleffbeef.pluggytesty.attributes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CraftListener implements Listener {
    private final AttributeManager attributeManager;

    public CraftListener(@NotNull AttributeManager attributeManager) {
        this.attributeManager = attributeManager;
    }

    @EventHandler
    public void onPrepareCraft(@NotNull PrepareItemCraftEvent event) {
        // Grab the crafting table inventory
        final CraftingInventory inventory = event.getInventory();
        // Grab the result slot
        final ItemStack result = inventory.getResult();

        // Exit if there isn't a result
        if (result == null)
            return;

        // If there is a result check if it needs to be updated
        attributeManager.updateItem(result);
    }

    /*@EventHandler
    public void onCraftItem(@NotNull CraftItemEvent event) {
        final CraftingInventory inventory = event.getInventory();
        final ItemStack result = inventory.getResult();

        if (result == null)
            return;

        attributeManager.updateItem(result);
        event.setResult(new Result);
    }*/
}

