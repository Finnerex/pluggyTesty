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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;

public class LifeDrainItemType extends SimpleItemType implements InteractableItemType {


    public LifeDrainItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.BONE);
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);
        meta.setLore(Arrays.asList(ChatColor.DARK_GREEN + "Right-Click : Life-steals nearby enemies", ChatColor.DARK_GREEN + "No Cooldown"));
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {

        final World w = player.getWorld();

        Collection<Entity> entities = player.getNearbyEntities(4, 2, 4);

        for (Entity e : entities) {

            if (e instanceof Damageable d) {
                float prevHealth = (float) d.getHealth();

                Vector vel = d.getVelocity();
                d.damage(2, player);
                d.setVelocity(vel); // no kb!!

                if (d.getHealth() < prevHealth) // don't heal the player if the target took no damage (i-frames)
                    player.setHealth(Math.min(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), (player.getHealth() + 0.5)));

                w.spawnParticle(Particle.DAMAGE_INDICATOR, e.getLocation(), 1);

                w.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 50);
            }
        }

        return true;
    }
}
