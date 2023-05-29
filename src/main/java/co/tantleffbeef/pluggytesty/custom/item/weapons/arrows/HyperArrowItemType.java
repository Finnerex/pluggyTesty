package co.tantleffbeef.pluggytesty.custom.item.weapons.arrows;

import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HyperArrowItemType extends SimpleItemType implements CustomArrow {
    public HyperArrowItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.TIPPED_ARROW);
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);
        meta.setLore(List.of(ChatColor.DARK_GREEN + "High velocity, pierces 8 enemies"));
        ((PotionMeta) meta).setColor(Color.fromRGB(210, 217, 24));
    }

    @Override
    public void applySpawnEffects(Arrow arrow) {
        arrow.setVelocity(arrow.getVelocity().multiply(2));
        arrow.setPierceLevel(arrow.getPierceLevel() + 8);
        arrow.setKnockbackStrength(0);
    }

    @Override
    public void applyLandingEffects(Arrow arrow, ProjectileHitEvent event) {/* no landing effects */}
}
