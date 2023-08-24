package co.tantleffbeef.pluggytesty.inventoryGUI;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

// a button where click is applied after clicking something else in the inventory.
public class InventorySelectorButton extends InventoryButton {

    public InventorySelectorButton(Consumer<InventoryClickEvent> clickEventConsumer, Material icon, String name, String... lore) {
        super(clickEventConsumer, icon, name, lore);
    }

    public InventorySelectorButton(Consumer<InventoryClickEvent> clickEventConsumer, ItemStack icon) {
        super(clickEventConsumer, icon);
    }

    @Override
    public void click(InventoryClickEvent event) {

    }
}
