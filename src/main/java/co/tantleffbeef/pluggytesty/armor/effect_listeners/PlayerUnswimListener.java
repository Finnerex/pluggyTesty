package co.tantleffbeef.pluggytesty.armor.effect_listeners;

import co.tantleffbeef.pluggytesty.armor.ArmorEffectType;
import co.tantleffbeef.pluggytesty.armor.ArmorEquipListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleSwimEvent;

public class PlayerUnswimListener implements Listener {

    @EventHandler
    public void onSwim(EntityToggleSwimEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;

        if(ArmorEquipListener.effectMap.get(player.getUniqueId()) != ArmorEffectType.CONDUIT_POWER
                || event.isSwimming() || !player.isSneaking())
            return;

        player.setVelocity(player.getVelocity().add(player.getLocation().getDirection()));

    }
}
