package co.tantleffbeef.pluggytesty.custom.item.weapons.arrows;

import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FrostburnArrowItemType extends SimpleItemType implements CustomArrow {

    public FrostburnArrowItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.TIPPED_ARROW);
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);
        meta.setLore(List.of(ChatColor.DARK_GREEN + "Freezes enemies"));
        ((PotionMeta) meta).setColor(Color.fromRGB(40, 130, 247));
    }

    @Override
    public void applySpawnEffects(Arrow arrow) {/* none */}

    @Override
    public void applyLandingEffects(Arrow arrow, ProjectileHitEvent event) {
        if (!(event.getHitEntity() instanceof Damageable damageable))
            return;

        damageable.setFreezeTicks(200);
    }
}
