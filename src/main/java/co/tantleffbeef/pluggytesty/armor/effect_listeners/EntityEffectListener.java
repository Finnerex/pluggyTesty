package co.tantleffbeef.pluggytesty.armor.effect_listeners;

import co.tantleffbeef.pluggytesty.armor.ArmorEffectType;
import co.tantleffbeef.pluggytesty.armor.ArmorEquipListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class EntityEffectListener implements Listener {

    @EventHandler
    public void onEffect(EntityPotionEffectEvent event) {
        Bukkit.broadcastMessage("effect event");
        if (!(event.getEntity() instanceof Player player))
            return;

        Bukkit.broadcastMessage("is player");

        if (ArmorEquipListener.effectMap.get(player.getUniqueId()) != ArmorEffectType.NIGHT_VISION)
            return;

        Bukkit.broadcastMessage("has effect" + event.getNewEffect().toString());

        if (event.getNewEffect().getType() == PotionEffectType.BLINDNESS || event.getNewEffect().getType() == PotionEffectType.DARKNESS) {
            Bukkit.broadcastMessage("blind or dark");
            event.setCancelled(true);
        }
    }

}
