package co.tantleffbeef.pluggytesty.inventoryGUI;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class InventoryGUIManager implements Listener {

    private static final Map<Inventory, InventoryGUI> inventories = new HashMap<>();

    public static void registerInventoryGUI(Inventory inventory, InventoryGUI gui) {
        inventories.put(inventory, gui);
    }

//    @EventHandler
//    public void OnInventoryOpen(InventoryOpenEvent event) {
//
//    }

    @EventHandler
    public void OnInventoryClick(InventoryClickEvent event) {
        InventoryGUI gui = inventories.get(event.getInventory());

        if (gui == null)
            return;

        event.setCancelled(true);
        InventoryButton button = gui.getButton(event.getSlot());
        button.click(event);
    }

    @EventHandler
    public void OnInventoryClose(InventoryCloseEvent event) {
        inventories.remove(event.getInventory());
    }

}
