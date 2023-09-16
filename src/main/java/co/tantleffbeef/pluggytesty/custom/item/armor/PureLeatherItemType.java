package co.tantleffbeef.pluggytesty.custom.item.armor;

import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class PureLeatherItemType extends SimpleItemType {
    public PureLeatherItemType(Plugin namespace, String id, boolean customModel, String name, Material baseItemMaterial) {
        super(namespace, id, customModel, name);
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta){
        super.modifyItemMeta(meta);

        meta.addEnchant(Enchantment.LURE, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }
}
