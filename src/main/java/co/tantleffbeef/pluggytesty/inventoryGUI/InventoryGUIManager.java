package co.tantleffbeef.pluggytesty.inventoryGUI;

import org.bukkit.Bukkit;
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

    // map for if selector buttons are listening for clicks
    private final Map<InventoryGUI, InventorySelectorButton> listeningForSelection = new HashMap<>();

    public static void registerInventoryGUI(Inventory inventory, InventoryGUI gui) {
        inventories.put(inventory, gui);
    }

    @EventHandler
    public void OnInventoryClick(InventoryClickEvent event) {
        InventoryGUI gui = inventories.get(event.getInventory());

        if (gui == null)
            return;

        event.setCancelled(true);

        InventoryButton button = gui.getButton(event.getSlot());

        if (button == null) {
            InventorySelectorButton selector = listeningForSelection.get(gui);
            Bukkit.broadcastMessage("not button");
            if (selector != null) {
                selector.click(event);
                Bukkit.broadcastMessage("second click on selector");

                listeningForSelection.remove(gui);
            }

            return;
        }


        if (button instanceof InventorySelectorButton selector) {
            listeningForSelection.put(gui, selector);
            Bukkit.broadcastMessage("selector click");
        } else {
            button.click(event);
            Bukkit.broadcastMessage("normal button click");
            listeningForSelection.remove(gui);
        }

    }

    @EventHandler
    public void OnInventoryClose(InventoryCloseEvent event) {
        inventories.remove(event.getInventory());
    }

}
