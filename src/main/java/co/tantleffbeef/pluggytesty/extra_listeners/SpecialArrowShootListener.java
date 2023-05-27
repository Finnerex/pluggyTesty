package co.tantleffbeef.pluggytesty.extra_listeners;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.pojo.serialize.CustomItemNbt;
import co.tantleffbeef.pluggytesty.custom.item.weapons.arrows.CustomArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class SpecialArrowShootListener implements Listener {

    private final KeyManager<CustomNbtKey> keyManager;
    private final ResourceManager resourceManager;

    public SpecialArrowShootListener(@NotNull KeyManager<CustomNbtKey> keyManager, @NotNull ResourceManager resourceManager) {
        this.keyManager = keyManager;
        this.resourceManager = resourceManager;
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
        customArrow.runCustomEffects(arrow);

    }



}
