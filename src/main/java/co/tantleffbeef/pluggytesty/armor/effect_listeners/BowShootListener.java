package co.tantleffbeef.pluggytesty.armor.effect_listeners;


import co.tantleffbeef.pluggytesty.armor.ArmorEffectType;
import co.tantleffbeef.pluggytesty.armor.ArmorEquipListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;
import java.util.UUID;

public class BowShootListener implements Listener {

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {
        Bukkit.broadcastMessage("shoot event");

        if (!(event.getEntity() instanceof Player player))
            return;

        UUID playerUUID = player.getUniqueId();

        if (ArmorEquipListener.effectMap.get(playerUUID) != ArmorEffectType.ARROW_CONSERVATION)
            return;

        if (new Random().nextInt(2) == 1) {
            player.getInventory().addItem(new ItemStack(Material.ARROW));
            //player.updateInventory();
        }

    }
}
