package co.tantleffbeef.pluggytesty.extra_listeners;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.pojo.serialize.CustomItemNbt;
import co.tantleffbeef.pluggytesty.attributes.AttributeManager;
import co.tantleffbeef.pluggytesty.custom.item.weapons.arrows.BouncyArrowItemType;
import co.tantleffbeef.pluggytesty.custom.item.weapons.arrows.CustomArrow;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.inventory.Inventory;
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

    @EventHandler // why the fuck doesn't it give you the entity of this event???
    public void onDispense(BlockDispenseEvent event) {

        Block dispenser = event.getBlock();
        if (dispenser.getType() != Material.DISPENSER)
            return;

        ItemStack item = event.getItem();

        CustomArrow customArrow = checkArrow(item);

        if (customArrow == null)
            return;

        event.setCancelled(true);

        Location location = dispenser.getLocation();
        location.setX(location.getX() + 0.5f);

        dispenser.getWorld().spawn(location.add(location.getDirection()), Arrow.class, (arrow) -> {
           arrow.setVelocity(event.getVelocity());
           customArrow.applySpawnEffects(arrow);
           arrow.setMetadata("customArrowType", new FixedMetadataValue(plugin, customArrow));
           arrow.setPickupStatus(AbstractArrow.PickupStatus.ALLOWED);
        });

        // this is fucking dumb
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            item.setAmount(item.getAmount() - 1);
//            BlockState state = dispenser.getState();
//            assert state instanceof Container;
//            Inventory inventory = ((Container) state).getInventory();
//            inventory.remove(item);
//            Bukkit.broadcastMessage("amount 1: " + item.getAmount());
//            item.setAmount(item.getAmount() - 1);
//            Bukkit.broadcastMessage("amount 2: " + item.getAmount());
//            inventory.addItem(item);
//            Bukkit.broadcastMessage("amount 3: " + inventory.getItem(inventory.first(item)).getAmount());
        });


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
