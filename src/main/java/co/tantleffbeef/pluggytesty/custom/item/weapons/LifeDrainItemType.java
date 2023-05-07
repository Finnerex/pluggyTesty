package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class LifeDrainItemType extends SimpleItemType implements InteractableItemType {


    public LifeDrainItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.SKELETON_SKULL);
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {

        final World w = player.getWorld();

        Collection<Entity> entities = player.getNearbyEntities(4, 2, 4);

        for (Entity e : entities) {

            if (e instanceof Damageable d) {
                float prevHealth = (float) d.getHealth();

                d.damage(2, player);

                if (d.getHealth() < prevHealth)
                    player.setHealth(Math.min(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), (player.getHealth() + 0.5)));

                w.spawnParticle(Particle.DAMAGE_INDICATOR, e.getLocation(), 1);

                w.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 50);
            }
        }

        return true;
    }
}
