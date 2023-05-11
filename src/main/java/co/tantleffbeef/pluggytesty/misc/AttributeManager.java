package co.tantleffbeef.pluggytesty.misc;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.pojo.serialize.CustomItemNbt;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
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
        else if (modified.getItemMeta() == null)
            throw new RuntimeException("Item registered with no item meta! id: " + id);

        // Mark down the item as modified
        modifiedItemSet.add(id);

        // Save the modified version
        itemModifications.put(id, modified.clone());
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
        // Grab the contents of the inventory
        final var contents = inventory.getContents();

        // Loop through all items in the inventory
        for (final ItemStack content : contents) {
            // Update each element of contents
            updateItem(content);
        }

        // Set the contents back to the inventory
        inventory.setContents(contents);
    }

    /**
     * Checks if the itemstack requires an update and updates it if necessary
     * @param itemStack the itemstack to check and update
     */
    public void updateItem(@NotNull ItemStack itemStack) {
        assert itemStack.getItemMeta() != null;

        // Grab the item's id
        final var id = CustomItemNbt.customItemIdOrVanilla(itemStack, keyManager);

        // If no modification is registered
        // for this item id then no need
        // to modify it
        if (!modifiedItemSet.contains(id))
            return;

        final var modification = itemModifications.get(id);
        assert modification != null;

        // If the item doesn't need modification return
        if (!needsModification(itemStack, modification))
            return;

        assert modification.getItemMeta() != null;

        itemStack.setItemMeta(
                resetMeta(itemStack.getItemMeta(), modification.getItemMeta())
        );
    }

    private boolean needsModification(@NotNull ItemStack toModify, @NotNull ItemStack modification) {
        final var originalMeta = toModify.getItemMeta();
        final var modifiedMeta = modification.getItemMeta();

        assert originalMeta != null;
        assert modifiedMeta != null;

        // Clone original meta so we can clear enchantments and durability
        final var clonedOriginalMeta = originalMeta.clone();
        // Reset durability and enchantments
        clearUniqueMeta(clonedOriginalMeta, toModify.getType());

        return modifiedMeta.equals(clonedOriginalMeta);
    }

    /**
     * Clears parts of the meta that aren't needed
     * like durability and enchantments
     */
    private void clearUniqueMeta(@NotNull ItemMeta toClear, @NotNull Material itemType) {
        // Grab the item's enchantments
        final var enchantmentList = toClear.getEnchants();
        // Loop through and remove them all
        for (final var enchantment : enchantmentList.keySet()) {
            toClear.removeEnchant(enchantment);
        }

        // check if has durability
        if (!(toClear instanceof Damageable damageable))
            return;

        // reset it if so
        damageable.setDamage(itemType.getMaxDurability());
    }

    private @NotNull ItemMeta resetMeta(@NotNull ItemMeta toReset, @NotNull ItemMeta model) {
        final var newMeta = model.clone();
        final var enchantments = toReset.getEnchants();
        for (final var enchantment : enchantments.entrySet()) {
            newMeta.addEnchant(enchantment.getKey(), enchantment.getValue(), true);
        }

        if (newMeta instanceof Damageable damageableMeta) {
            final var damage = ((Damageable) toReset).getDamage();
            damageableMeta.setDamage(damage);
        }

        return newMeta;
    }
}
