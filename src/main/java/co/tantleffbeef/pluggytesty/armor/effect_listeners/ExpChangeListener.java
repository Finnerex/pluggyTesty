package co.tantleffbeef.pluggytesty.armor.effect_listeners;

import co.tantleffbeef.pluggytesty.armor.ArmorEffectType;
import co.tantleffbeef.pluggytesty.armor.ArmorEquipListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import java.util.UUID;

public class ExpChangeListener implements Listener {

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (ArmorEquipListener.effectMap.get(playerUUID) != ArmorEffectType.EXP_BOOST)
            return;

        event.setAmount(event.getAmount() * 2);
    }
}
