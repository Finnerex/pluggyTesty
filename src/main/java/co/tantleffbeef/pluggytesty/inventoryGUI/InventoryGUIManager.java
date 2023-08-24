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
import java.util.UUID;

public class InventoryGUIManager implements Listener {

    private static final Map<UUID, InventoryGUI> inventories = new HashMap<>();

    // map for if selector buttons are listening for clicks
    private final Map<UUID, InventorySelectorButton> listeningForSelection = new HashMap<>();

    public static void registerInventoryGUI(UUID player, InventoryGUI gui) {
        inventories.put(player, gui);
    }

    @EventHandler
    public void OnInventoryClick(InventoryClickEvent event) {
        UUID player = event.getWhoClicked().getUniqueId();
        InventoryGUI gui = inventories.get(player);

        Bukkit.broadcastMessage("inv opened");

        if (gui == null)
            return;

        event.setCancelled(true);

        InventoryButton button = gui.getButton(event.getSlot(), event.getClickedInventory());

        if (button == null) {

            InventorySelectorButton selector = listeningForSelection.get(player);
            if (selector != null) {
                selector.click(event);

                Bukkit.broadcastMessage("selector?????");

                listeningForSelection.remove(player);
            }

            return;
        }

        if (button instanceof InventorySelectorButton selector) {
            listeningForSelection.put(player, selector);
        } else {
            button.click(event);
            listeningForSelection.remove(player);
        }

    }

    @EventHandler
    public void OnInventoryClose(InventoryCloseEvent event) {
        inventories.remove(event.getPlayer().getUniqueId());
    }

}
