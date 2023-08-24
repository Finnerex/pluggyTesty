package co.tantleffbeef.pluggytesty.extra_listeners.custom_items;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import co.tantleffbeef.pluggytesty.custom.item.weapons.RandomEffectBowItemType;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Random;


public class RandomEffectBowInteractListener implements Listener {

    private final KeyManager<CustomNbtKey> keyManager;
    private final ResourceManager resourceManager;

    public RandomEffectBowInteractListener(@NotNull KeyManager<CustomNbtKey> keyManager, @NotNull ResourceManager resourceManager) {
        this.keyManager = keyManager;
        this.resourceManager = resourceManager;
    }

    @EventHandler
    private void onEntityShootBow(EntityShootBowEvent event) {

        Entity entity = event.getEntity();

        if(!(entity instanceof Player)) {
            return;
        }

        if (event.isCancelled())
            return;

        if (CustomItemType.asInstanceOf(RandomEffectBowItemType.class, event.getBow(), keyManager, resourceManager) == null)
            return;

        Arrow arrow = (Arrow) event.getProjectile();

        int effect = new Random().nextInt(12);
        boolean hasEffect = true;

        switch (effect) {
            case 0 -> arrow.addCustomEffect(PotionEffectType.BLINDNESS.createEffect(100, 1), false);
            case 1 -> arrow.addCustomEffect(PotionEffectType.HARM.createEffect(1, 1), false);
            case 2 -> arrow.addCustomEffect(PotionEffectType.HARM.createEffect(1, 2), false);
            case 3 -> arrow.addCustomEffect(PotionEffectType.POISON.createEffect(100, 1), false);
            case 4 -> arrow.addCustomEffect(PotionEffectType.POISON.createEffect(40, 2), false);
            case 5 -> arrow.addCustomEffect(PotionEffectType.SLOW.createEffect(60, 4), false);
            default -> hasEffect = false;
        }

        if (hasEffect)
            arrow.setPickupStatus(AbstractArrow.PickupStatus.CREATIVE_ONLY);

        event.setProjectile(arrow);
    }
}
