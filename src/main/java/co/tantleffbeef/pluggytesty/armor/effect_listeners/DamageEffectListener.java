package co.tantleffbeef.pluggytesty.armor.effect_listeners;

import co.tantleffbeef.pluggytesty.armor.ArmorEffectType;
import co.tantleffbeef.pluggytesty.armor.ArmorEquipListener;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;


public class DamageEffectListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player) || !(event.getEntity() instanceof LivingEntity entity))
            return;

        UUID playerUUID = player.getUniqueId();

        ArmorEffectType effect = ArmorEquipListener.effectMap.get(playerUUID);

        if (effect == ArmorEffectType.DAMAGE_INCREASE)
            event.setDamage(event.getDamage() * 50);
        else if (effect == ArmorEffectType.WITHER_ATTACKS)
            entity.addPotionEffect(PotionEffectType.WITHER.createEffect(40, 1));

    }

}
