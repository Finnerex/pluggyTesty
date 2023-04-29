package co.tantleffbeef.pluggytesty.armor.effect_listeners;

import co.tantleffbeef.pluggytesty.armor.ArmorEffectType;
import co.tantleffbeef.pluggytesty.armor.ArmorEquipListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class EntityEffectListener implements Listener {

    @EventHandler
    public void onEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;

        if (ArmorEquipListener.effectMap.get(player.getUniqueId()) != ArmorEffectType.NIGHT_VISION)
            return;

        if (event.getNewEffect().getType() != PotionEffectType.BLINDNESS || event.getNewEffect().getType() != PotionEffectType.DARKNESS)
            return;

        event.setCancelled(true);
    }

}
