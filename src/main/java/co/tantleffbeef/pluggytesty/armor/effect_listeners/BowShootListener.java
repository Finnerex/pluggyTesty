package co.tantleffbeef.pluggytesty.armor.effect_listeners;

import co.tantleffbeef.pluggytesty.armor.ArmorEffectType;
import co.tantleffbeef.pluggytesty.armor.ArmorEquipListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.Random;
import java.util.UUID;

public class BowShootListener implements Listener {

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {

        if (!(event.getEntity() instanceof Player player))
            return;

        UUID playerUUID = player.getUniqueId();

        if (ArmorEquipListener.effectMap.get(playerUUID) != ArmorEffectType.ARROW_CONSERVATION)
            return;

        if (new Random().nextBoolean()) {
            event.setConsumeItem(false);
            player.updateInventory();
        }

    }
}
