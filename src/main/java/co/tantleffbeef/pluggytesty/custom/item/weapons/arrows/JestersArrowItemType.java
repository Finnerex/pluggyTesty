package co.tantleffbeef.pluggytesty.custom.item.weapons.arrows;

import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class JestersArrowItemType extends SimpleItemType implements CustomArrow {
    public JestersArrowItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.TIPPED_ARROW);
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);
        ((PotionMeta) meta).setColor(Color.fromRGB(221, 187, 237));
    }

    @Override
    public void runCustomEffects(Arrow arrow) {
        arrow.setVelocity(arrow.getLocation().getDirection().multiply(4));
        arrow.setPierceLevel(10);
        arrow.setKnockbackStrength(0);
    }
}
