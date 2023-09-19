package co.tantleffbeef.pluggytesty.attributes;
import co.tantleffbeef.pluggytesty.custom.item.armor.SimpleArmorItemType;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AttributeUpdateListener implements Listener {
    private final AttributeManager attributeManager;

    public AttributeUpdateListener(@NotNull AttributeManager attributeManager) {
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
    @EventHandler
    public void onPrepareSmithingCraft(@NotNull PrepareSmithingEvent event) {
        // Grab the smithing table inventory
        final SmithingInventory inventory = event.getInventory();
        // Grab the result slot
        final ItemStack result = inventory.getResult();
        // Grab the recipe
        final var recipe = inventory.getRecipe();

        // Exit if there isn't a result
        if (result == null)
            return;

        if (recipe == null)
            return;

        // make sure player isn't dumbo
        if (inventory.contains(result))
            return;

        // If there is a result check if it needs to be updated
        attributeManager.updateSmithingItem(result, recipe, event);
    }

    @EventHandler
    public void onInventoryOpen(@NotNull InventoryOpenEvent event) {
        final var inventory = event.getInventory();

        attributeManager.checkInventory(inventory);
    }

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        final var inventory = event.getPlayer().getInventory();

        attributeManager.checkInventory(inventory);
    }

    @EventHandler
    public void onEntityPickupItem(@NotNull EntityPickupItemEvent event) {
        final var item = event.getItem();
        final var itemStack = item.getItemStack();

        attributeManager.updateItem(itemStack);
        item.setItemStack(itemStack);
    }
}

