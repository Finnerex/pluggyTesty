package co.tantleffbeef.pluggytesty.custom.item.utility;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import co.tantleffbeef.pluggytesty.inventoryGUI.InventoryButton;
import co.tantleffbeef.pluggytesty.inventoryGUI.InventoryGUI;
import co.tantleffbeef.pluggytesty.inventoryGUI.InventorySelectorButton;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArrowBeltItemType extends SimpleItemType implements InteractableItemType {

    private final Map<UUID, InventoryGUI> playerBelts;

    private final InventoryGUI DEFAULT_BELT;

    public ArrowBeltItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.PAPER);

        playerBelts = new HashMap<>();

        DEFAULT_BELT = new InventoryGUI(9, "ArrowBelt", namespace.getServer());


        for (int i = 2; i < 7; i++) {
            int buttonSlot = i;
            InventoryButton selectorButton = new InventorySelectorButton(
                    (event) -> {
                        ItemStack item = event.getCurrentItem();
                        event.setCancelled(true);

                        if (item == null)
                            return;

                        if (item.getType() == Material.ARROW || item.getType() == Material.SPECTRAL_ARROW || item.getType() == Material.TIPPED_ARROW) {
                            item.setAmount(1);
                            playerBelts.get(event.getWhoClicked().getUniqueId()).setIcon(buttonSlot, item);

                            if (event.getWhoClicked() instanceof Player player)
                                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 2);
                        }

                    },
                    Material.GRAY_STAINED_GLASS_PANE,
                    "No arrow assigned",
                    "Click to change"
            );

            DEFAULT_BELT.addButton(selectorButton, i);
        }
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {
        UUID uuid = player.getUniqueId();

        playerBelts.putIfAbsent(uuid, DEFAULT_BELT);

        playerBelts.get(uuid).displayTo(player);

        return false;
    }
}
