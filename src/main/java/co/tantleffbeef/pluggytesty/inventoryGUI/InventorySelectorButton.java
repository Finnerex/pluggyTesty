package co.tantleffbeef.pluggytesty.inventoryGUI;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

// I don't really see the point of this class, but it is very likely that nobody will look
// at this and notice that this class is pointless and there is a better way of doing this.
// It works in its current state, I will not change it.
// a button where click is applied after clicking something else in the inventory.
public class InventorySelectorButton extends InventoryButton {

    public InventorySelectorButton(Consumer<InventoryClickEvent> clickEventConsumer, Material icon, String name, String... lore) {
        super(clickEventConsumer, icon, name, lore);
    }

    public InventorySelectorButton(Consumer<InventoryClickEvent> clickEventConsumer, ItemStack icon) {
        super(clickEventConsumer, icon);
    }

}
