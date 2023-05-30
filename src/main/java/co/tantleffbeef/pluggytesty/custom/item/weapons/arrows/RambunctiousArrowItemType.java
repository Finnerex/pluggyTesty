package co.tantleffbeef.pluggytesty.custom.item.weapons.arrows;

import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RambunctiousArrowItemType extends SimpleItemType implements CustomArrow {

    private final Plugin plugin;

    public RambunctiousArrowItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.TIPPED_ARROW);
        this.plugin = namespace;
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);
        meta.setLore(List.of(ChatColor.DARK_GREEN + "High velocity, pierces 6 enemies, bounces"));
        ((PotionMeta) meta).setColor(Color.fromRGB(24, 217, 47));
    }

    @Override
    public void applySpawnEffects(Arrow arrow) {
        arrow.setVelocity(arrow.getVelocity().multiply(1.8f));
        arrow.setPierceLevel(arrow.getPierceLevel() + 6);
        arrow.setKnockbackStrength(0);
        arrow.setBounce(true);
    }

    @Override
    public void applyLandingEffects(Arrow arrow, ProjectileHitEvent event) {
        // no kb
        Entity entity = event.getHitEntity();
        if (entity == null)
            return;

        final Vector entityVelocity = entity.getVelocity();

        plugin.getServer().getScheduler().runTask(plugin, () -> entity.setVelocity(entityVelocity));

        // bounce
        final Vector velocity = arrow.getVelocity();

        if (velocity.length() < 0.6f)
            return;

        final BlockFace face = event.getHitBlockFace();

        if (face == null)
            return;

        switch (face) {
            case UP, DOWN -> velocity.setY(velocity.getY() * -1);
            case EAST, WEST -> velocity.setX(velocity.getX() * -1);
            case NORTH, SOUTH -> velocity.setZ(velocity.getZ() * -1);
        }

        velocity.multiply(0.85f);

        arrow.getWorld().spawn(arrow.getLocation(), Arrow.class, (projectile) -> {
            projectile.setVelocity(velocity);
            projectile.setShooter(arrow.getShooter());

            // set this to have the same metadata (why does this have to be so big)
            for (MetadataValue data : arrow.getMetadata("customArrowType")){
                if (data.value() instanceof CustomArrow customArrow) {
                    projectile.setMetadata("customArrowType", new FixedMetadataValue(plugin, customArrow));
                    break;
                }
            }

        });

        arrow.remove();
    }
}
