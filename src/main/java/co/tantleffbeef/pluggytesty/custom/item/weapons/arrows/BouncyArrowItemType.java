package co.tantleffbeef.pluggytesty.custom.item.weapons.arrows;

import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BouncyArrowItemType extends SimpleItemType implements CustomArrow {
    public BouncyArrowItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.TIPPED_ARROW);
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);
        meta.setLore(List.of(ChatColor.DARK_GREEN + "Higher velocity, bounces"));
        ((PotionMeta) meta).setColor(Color.fromRGB(221, 187, 237));
    }

    @Override
    public void runCustomEffects(Arrow arrow) {
        arrow.setVelocity(arrow.getVelocity().multiply(1.5f));
        arrow.setBounce(true);
    }
}
