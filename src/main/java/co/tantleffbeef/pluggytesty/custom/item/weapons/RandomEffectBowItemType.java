package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class RandomEffectBowItemType extends SimpleItemType {

    public RandomEffectBowItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.BOW);
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);
        meta.setLore(Arrays.asList(ChatColor.DARK_GREEN + "Chance to shoot a debuff-tipped arrow"));
    }

}
