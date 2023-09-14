package co.tantleffbeef.pluggytesty.custom.item.armor;

import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class SimpleArmorItemType extends SimpleItemType {
    public record AttributePair(Attribute attribute, AttributeModifier attributeModifier) {
    }

    protected final AttributePair[] attributePairs;

    public SimpleArmorItemType(@NotNull Plugin namespace, @NotNull String id, boolean customModel, String name, Material baseItemMaterial, AttributePair... attributePairs){
        super(namespace, id, customModel, name, baseItemMaterial);

        this.attributePairs = attributePairs;
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);

        for (AttributePair pair : attributePairs) {
            meta.addAttributeModifier(pair.attribute, pair.attributeModifier);
        }
    }
}
