package co.tantleffbeef.pluggytesty.extra_listeners;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.pojo.serialize.CustomItemNbt;
import co.tantleffbeef.pluggytesty.attributes.AttributeManager;
import co.tantleffbeef.pluggytesty.custom.item.weapons.arrows.BouncyArrowItemType;
import co.tantleffbeef.pluggytesty.custom.item.weapons.arrows.CustomArrow;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

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

        ItemStack item = event.getConsumable();
        if (item == null)
            return;

        // figure out if this arrow is a custom arrow
        CustomArrow customArrow = checkArrow(item);

        if (customArrow == null)
            return;

        // interfaces go hard!!!
        customArrow.applySpawnEffects(arrow);

        // so the arrow can be identified upon landing
        arrow.setMetadata("customArrowType", new FixedMetadataValue(plugin, customArrow));

    }

    @EventHandler // why the fuck doesn't it give you the item???
    public void onDispense(BlockDispenseEvent event) {
        Bukkit.broadcastMessage("deispend");

        if (!(event.getBlock() instanceof Dispenser dispenser))
            return;

        Bukkit.broadcastMessage("is dispendersd");

        ItemStack item = event.getItem();

        CustomArrow customArrow = checkArrow(item);

        if (customArrow == null)
            return;

        Bukkit.broadcastMessage("is custom " + customArrow);

        event.setCancelled(true);

        dispenser.getWorld().spawn(dispenser.getLocation().add(dispenser.getLocation().getDirection()), Arrow.class, (arrow) -> {
           arrow.setVelocity(event.getVelocity());
           customArrow.applySpawnEffects(arrow);
           arrow.setMetadata("customArrowType", new FixedMetadataValue(plugin, customArrow));
        });

        item.setAmount(item.getAmount() - 1);
    }

    private CustomArrow checkArrow(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return null;

        final var data = meta.getPersistentDataContainer();

        if (!CustomItemNbt.hasCustomItemNbt(data, keyManager))
            return null;

        final var itemNbt = CustomItemNbt.fromPersistentDataContainer(data, keyManager);
        final var itemType = resourceManager.getCustomItemType(itemNbt.id);


        if (!(itemType instanceof CustomArrow customArrow))
            return null;

        return customArrow;
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
