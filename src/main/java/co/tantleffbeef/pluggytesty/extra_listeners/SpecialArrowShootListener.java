package co.tantleffbeef.pluggytesty.extra_listeners;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import co.tantleffbeef.pluggytesty.custom.item.weapons.arrows.CustomArrow;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
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

    @EventHandler(priority = EventPriority.LOW)
    public void onBowShoot(EntityShootBowEvent event) {

        if (!(event.getProjectile() instanceof Arrow arrow) || event.isCancelled())
            return;

        ItemStack item = event.getConsumable();
        if (item == null)
            return;

        // figure out if this arrow is a custom arrow
        CustomArrow customArrow = CustomItemType.asInstanceOf(CustomArrow.class, event.getConsumable(), keyManager, resourceManager);

        if (customArrow == null)
            return;

        // interfaces go hard!!!
        customArrow.applySpawnEffects(arrow);

        // so the arrow can be identified upon landing
        arrow.setMetadata("customArrowType", new FixedMetadataValue(plugin, customArrow));

    }

    @EventHandler
    public void onArrowLand(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Arrow arrow))
            return;

        // loop through this even though there will be only one
        for (MetadataValue data : arrow.getMetadata("customArrowType")) {
            if (data.value() instanceof CustomArrow customArrow) {
                customArrow.applyLandingEffects(arrow, event);
                break;
            }
        }

    }

    @EventHandler
    public void onPickup(PlayerPickupArrowEvent event) {
        for (MetadataValue data : event.getArrow().getMetadata("customArrowType")) {
            if (data.value() instanceof CustomArrow customArrow) {
                Player player = event.getPlayer();

                event.setCancelled(true);

                player.getInventory().addItem(resourceManager.getCustomItemStack(customArrow.id()));
                player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

                event.getArrow().remove();
            }
        }
    }



}
