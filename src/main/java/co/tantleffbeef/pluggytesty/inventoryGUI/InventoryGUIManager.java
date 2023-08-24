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
    private final Map<Inventory, InventorySelectorButton> listeningForSelection = new HashMap<>();

    public static void registerInventoryGUI(Inventory inventory, InventoryGUI gui) {
        inventories.put(inventory, gui);
    }

    @EventHandler
    public void OnInventoryClick(InventoryClickEvent event) {
        Inventory wholeInv = event.getInventory();

        Inventory inv = event.getClickedInventory();
        InventoryGUI gui = inventories.get(inv);

        Bukkit.broadcastMessage("inv opened");

        if (gui == null) {
            InventorySelectorButton selector = listeningForSelection.get(wholeInv);
            if (selector != null) {
                selector.click(event);
                
                Bukkit.broadcastMessage("selector?????");

                listeningForSelection.remove(wholeInv);
            }

            return;
        }

        Bukkit.broadcastMessage("gui not null");

        event.setCancelled(true);

        InventoryButton button = gui.getButton(event.getSlot());

        if (button == null)
            return;

        if (button instanceof InventorySelectorButton selector) {
            listeningForSelection.put(wholeInv, selector);
        } else {
            button.click(event);
            listeningForSelection.remove(wholeInv);
        }

    }

    @EventHandler
    public void OnInventoryClose(InventoryCloseEvent event) {
        inventories.remove(event.getInventory());
    }

}
