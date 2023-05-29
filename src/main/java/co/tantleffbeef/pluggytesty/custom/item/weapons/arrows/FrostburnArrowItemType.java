package co.tantleffbeef.pluggytesty.custom.item.weapons.arrows;

import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.Plugin;

public class FrostburnArrowItemType extends SimpleItemType implements CustomArrow {

    public FrostburnArrowItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.TIPPED_ARROW);
    }

    @Override
    public void applySpawnEffects(Arrow arrow) {/* none */}

    @Override
    public void applyLandingEffects(Arrow arrow, ProjectileHitEvent event) {
        if (!(event.getHitEntity() instanceof Damageable damageable))
            return;

        damageable.setFreezeTicks(60);
    }
}
