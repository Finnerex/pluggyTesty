package co.tantleffbeef.pluggytesty.misc;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.pojo.serialize.CustomItemNbt;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AttributeManager {
    // Set of all items that have been modifying
    // for quickly checking if they need to be modified
    private final Set<NamespacedKey> modifiedItemSet;

    // Map of each item to an itemstack that can
    // be copied
    private final Map<NamespacedKey, ItemStack> itemModifications;

    private final KeyManager<CustomNbtKey> keyManager;

    public AttributeManager(@NotNull KeyManager<CustomNbtKey> nbtKeyManager) {
        this.modifiedItemSet = new HashSet<>();
        this.itemModifications = new HashMap<>();
        this.keyManager = nbtKeyManager;
    }

    /**
     * Registers this itemStack as the model for how
     * to modify items of this type (custom item or
     * default minecraft one)
     * @param modified the item to use as a model for how to modify an item
     */
    public void registerModifiedItem(@NotNull ItemStack modified) {
        final var id = CustomItemNbt.customItemIdOrVanilla(modified, keyManager);

        // Make sure this item hasn't already been registered
        if (modifiedItemSet.contains(id))
            throw new RuntimeException("Same item registered twice as a modified item! id: " + id);

        // Mark down the item as modified
        modifiedItemSet.add(id);

        // Save the modified version
        itemModifications.put(id, modified);
    }

    /**
     * Scans through all items in the inventory,
     * seeing if they are upgradable and need to be upgraded
     * and upgrading them if so
     * <b>You still need to set this inventory back
     *  to whatever you got it from</b>
     * @param inventory the inventory to check
     */
    public void checkInventory(@NotNull Inventory inventory) {
        final var contents = inventory.getContents();

        // TODO: make work
    }

    public void updateItem(@NotNull ItemStack itemStack) {
        // TODO
    }
}
