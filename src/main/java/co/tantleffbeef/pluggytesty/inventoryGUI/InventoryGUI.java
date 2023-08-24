package co.tantleffbeef.pluggytesty.inventoryGUI;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

        inventory = server.createInventory(null, size, name);

        if (fillEmpty.getType() == Material.AIR)
            return;

        ItemMeta meta = fillEmpty.getItemMeta();

        meta.setDisplayName(" ");
        meta.setLore(List.of(" "));

        fillEmpty.setItemMeta(meta);

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, fillEmpty);
        }
    }

    public InventoryGUI addButton(InventoryButton button, int slot) {
        buttons.put(slot, button);
        inventory.setItem(slot, button.getIcon());
        return this;
    }

    public void setIcon(int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(List.of(lore));
        item.setItemMeta(meta);

        setIcon(slot, item);
    }

    public void setIcon(int slot, ItemStack itemStack) {
        getButton(slot).setIcon(itemStack);

        inventory.setItem(slot, itemStack);
    }

    public InventoryButton getButton(int slot) {
        return buttons.get(slot);
    }

    public void displayTo(Player player) {
        InventoryGUIManager.registerInventoryGUI(inventory, this);
        player.openInventory(inventory);
    }

}
