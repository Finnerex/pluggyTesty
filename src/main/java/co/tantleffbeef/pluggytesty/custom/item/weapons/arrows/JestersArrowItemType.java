package co.tantleffbeef.pluggytesty.custom.item.weapons.arrows;

import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JestersArrowItemType extends SimpleItemType implements CustomArrow {
    public JestersArrowItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.TIPPED_ARROW);
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);
        meta.setLore(List.of(ChatColor.DARK_GREEN + "High velocity, pierces up to 5 enemies"));
        ((PotionMeta) meta).setColor(Color.fromRGB(221, 187, 237));
    }

    @Override
    public void runSpawnEffects(Arrow arrow) {
        arrow.setVelocity(arrow.getVelocity().multiply(2));
        arrow.setPierceLevel(5);
        arrow.setKnockbackStrength(0);
    }
}
