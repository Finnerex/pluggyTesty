package co.tantleffbeef.pluggytesty.custom.item.armor;

import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CustomItemArmorType implements CustomItemType {
    private final NamespacedKey id;
    private final Material material;
    private final NamespacedKey customModel;
    private final String name;
    private final Consumer<ItemMeta> armorAttributesCallback;

    public CustomItemArmorType(Plugin namespace, String id, Material material, boolean model, String name, @NotNull Consumer<ItemMeta> setArmorAttributes) {
        this.id = new NamespacedKey(namespace, id);
        this.material = material;
        this.customModel = model ? new NamespacedKey(namespace, "item/armor/" + this.id.getKey()) : null;
        this.name = name;
        this.armorAttributesCallback = setArmorAttributes;
    }

    @Override
    public @NotNull Material baseMaterial() {
        return material;
    }

    @Override
    public @NotNull NamespacedKey id() {
        return id;
    }

    @Override
    public @Nullable NamespacedKey model() {
        return customModel;
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta itemMeta) {
        itemMeta.setDisplayName(name);

        armorAttributesCallback.accept(itemMeta);
    }
}
