package co.tantleffbeef.pluggytesty.armor.effect_listeners;


import co.tantleffbeef.pluggytesty.armor.ArmorEffectType;
import co.tantleffbeef.pluggytesty.armor.ArmorEquipListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class EntityDeathListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!(event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent damageEvent))
            return;

        if (!(damageEvent.getDamager() instanceof Player player))
            return;

        UUID playerUUID = player.getUniqueId();

        if (ArmorEquipListener.effectMap.get(playerUUID) == ArmorEffectType.REGEN_ON_KILL)
            player.addPotionEffect(PotionEffectType.REGENERATION.createEffect(60, 1)); // regen 2 for 3 secs

    }

}
