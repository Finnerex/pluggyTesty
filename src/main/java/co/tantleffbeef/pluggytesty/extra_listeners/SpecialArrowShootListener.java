package co.tantleffbeef.pluggytesty.extra_listeners;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.pojo.serialize.CustomItemNbt;
import co.tantleffbeef.pluggytesty.custom.item.weapons.arrows.BouncyArrowItemType;
import co.tantleffbeef.pluggytesty.custom.item.weapons.arrows.CustomArrow;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class SpecialArrowShootListener implements Listener {

    private final KeyManager<CustomNbtKey> keyManager;
    private final ResourceManager resourceManager;
    private final Plugin plugin;

    public SpecialArrowShootListener(@NotNull KeyManager<CustomNbtKey> keyManager, @NotNull ResourceManager resourceManager, @NotNull Plugin plugin) {
        this.keyManager = keyManager;
        this.resourceManager = resourceManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {

        if (!(event.getProjectile() instanceof Arrow arrow))
            return;

        // figure out if this arrow is a custom arrow
        ItemMeta meta = event.getConsumable().getItemMeta();
        if (meta == null)
            return;

        final var data = meta.getPersistentDataContainer();

        if (!CustomItemNbt.hasCustomItemNbt(data, keyManager))
            return;

        final var itemNbt = CustomItemNbt.fromPersistentDataContainer(data, keyManager);
        final var itemType = resourceManager.getCustomItemType(itemNbt.id);


        if (!(itemType instanceof CustomArrow customArrow))
            return;

        // interfaces go hard!!!
        customArrow.applySpawnEffects(arrow);

        // Idk about metadata but I think it works
        if (customArrow instanceof BouncyArrowItemType)
            arrow.setMetadata("bouncy", new FixedMetadataValue(plugin, true)); // value here doesn't mean anything, just if it has the data or not

    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onArrowLand(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Arrow arrow))
            return;

        if (!arrow.hasMetadata("bouncy"))
            return;

        Vector velocity = arrow.getVelocity();

        if (velocity.length() < 0.1f)
            return;

        BlockFace face = arrow.getFacing();

        if (face == null)
            return;

        switch (face) {
            case UP, DOWN -> velocity.setY(velocity.getY() * -1);
            case EAST, WEST -> velocity.setX(velocity.getX() * -1);
            case NORTH, SOUTH -> velocity.setZ(velocity.getZ() * -1);
        }

        velocity.multiply(0.9f);

        arrow.getWorld().spawn(arrow.getLocation(), Arrow.class, (projectile) -> {
            projectile.setVelocity(velocity);
            projectile.setShooter(arrow.getShooter());
            projectile.setMetadata("bouncy", new FixedMetadataValue(plugin, true));
        });

        arrow.remove();

    }



}
