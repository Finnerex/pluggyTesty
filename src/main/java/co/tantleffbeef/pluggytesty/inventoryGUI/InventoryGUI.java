package co.tantleffbeef.pluggytesty.inventoryGUI;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class InventoryGUI {
    private final Inventory inventory;
    private final Map<Integer, InventoryButton> buttons;


    public InventoryGUI(int size, String name, Server server) {
        this(size, name, new ItemStack(Material.AIR), server);
    }

    public InventoryGUI(int size, String name, Material fillEmpty, Server server) {
        this(size, name, new ItemStack(fillEmpty), server);
    }

    public InventoryGUI(int size, String name, ItemStack fillEmpty, Server server) {
        buttons = new HashMap<>();

        ItemMeta meta = fillEmpty.getItemMeta();

        meta.setDisplayName(null);
        meta.setLore(null);

        fillEmpty.setItemMeta(meta);

        inventory = server.createInventory(null, size, name);
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, fillEmpty);
        }
    }

    public InventoryGUI addButton(InventoryButton button, int slot) {
        buttons.put(slot, button);
        inventory.setItem(slot, button.getIcon());
        return this;
    }

    public InventoryButton getButton(int slot) {
        return buttons.get(slot);
    }

    public void displayTo(Player player) {
        InventoryGUIManager.registerInventoryGUI(inventory, this);
        player.openInventory(inventory);
    }

}
