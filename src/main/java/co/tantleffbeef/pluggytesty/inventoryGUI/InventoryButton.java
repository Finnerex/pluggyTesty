package co.tantleffbeef.pluggytesty.inventoryGUI;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class InventoryButton {

    private final Consumer<InventoryClickEvent> clickEventConsumer;
    private ItemStack icon;

    public InventoryButton(Consumer<InventoryClickEvent> clickEventConsumer, Material icon, String name, String... lore) {
        this(clickEventConsumer, new ItemStack(icon));

        ItemMeta meta = this.icon.getItemMeta();

        if (meta == null)
            return;

        meta.setLore(List.of(lore));
        meta.setDisplayName(name);

        this.icon.setItemMeta(meta);
    }


    public InventoryButton(Consumer<InventoryClickEvent> clickEventConsumer, ItemStack icon) {
        this.clickEventConsumer = clickEventConsumer;
        this.icon = icon;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public void setLore(String... lore) {
        ItemMeta meta = icon.getItemMeta();
        meta.setLore(Arrays.asList(lore));
        icon.setItemMeta(meta);
    }

    public void setName(String name) {
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(name);
        icon.setItemMeta(meta);
    }

    public void click(InventoryClickEvent event) {
        clickEventConsumer.accept(event);
    }

}
