package co.tantleffbeef.pluggytesty.armor.effect_listeners;

import co.tantleffbeef.pluggytesty.armor.ArmorEffectType;
import co.tantleffbeef.pluggytesty.armor.ArmorEquipListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
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

        PotionEffectType effect = event.getNewEffect().getType();
        Bukkit.broadcastMessage("has effect");
        Bukkit.broadcastMessage("blind " + (effect.equals(PotionEffectType.BLINDNESS)) + player.hasPotionEffect(PotionEffectType.BLINDNESS));
        Bukkit.broadcastMessage("dark " + (effect.equals(PotionEffectType.DARKNESS)) + player.hasPotionEffect(PotionEffectType.DARKNESS));

//event.getNewEffect().getType() == PotionEffectType.BLINDNESS || event.getNewEffect().getType() == PotionEffectType.DARKNESS
        if (player.hasPotionEffect(PotionEffectType.BLINDNESS) || player.hasPotionEffect(PotionEffectType.DARKNESS)) {
            Bukkit.broadcastMessage("blind or dark");
            //event.setCancelled(true);
        }
    }

}
