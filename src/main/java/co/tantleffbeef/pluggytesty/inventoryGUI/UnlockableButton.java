package co.tantleffbeef.pluggytesty.inventoryGUI;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.function.Consumer;

public class UnlockableButton extends InventoryButton {

    private Material unlockIcon;

    public UnlockableButton(Consumer<InventoryClickEvent> clickEventConsumer, Material icon, Material lockedIcon, String name, boolean obfuscate, String... lore) {
        super(clickEventConsumer, lockedIcon, (obfuscate ? ChatColor.MAGIC : "") + name, obfuscate ? (Arrays.stream(lore).map(s -> ChatColor.MAGIC + s).toArray(String[]::new)) : lore);
        this.unlockIcon = icon;
    }

    public void unlock() {
        getIcon().setType(unlockIcon);
        ItemMeta iconMeta = getIcon().getItemMeta();

        iconMeta.setDisplayName(iconMeta.getDisplayName().replaceFirst(ChatColor.MAGIC.toString(), ""));

        getIcon().setItemMeta(iconMeta);
    }
}
