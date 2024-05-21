package co.tantleffbeef.pluggytesty.extra_listeners.custom_items;

import co.tantleffbeef.pluggytesty.armor.ArmorEffectType;
import co.tantleffbeef.pluggytesty.armor.ArmorEquipListener;
import co.tantleffbeef.pluggytesty.custom.item.weapons.AxeOfYourMotherItemType;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

public class AxeFallDamageListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player) || event.getCause() != EntityDamageEvent.DamageCause.FALL)
            return;

        UUID playerUUID = player.getUniqueId();



        if (!AxeOfYourMotherItemType.thangList.contains(playerUUID))
            return;

        AxeOfYourMotherItemType.thangList.remove(playerUUID);

        applyAreaEffectDamage(player);
        event.setCancelled(true);

    }

    private void applyAreaEffectDamage(@NotNull Player player) {
        Location location = player.getLocation();
        assert location.getWorld() != null; // Player should have a world I hope
        final var entities = location.getWorld().getNearbyEntities(location, 2.5, 2, 2.5);

        for (Entity entity : entities) {
            if (entity instanceof Damageable damageable && !entity.equals(player))
                damageable.damage(7, player);
        }

        location.getWorld().playSound(location, Sound.BLOCK_ANVIL_LAND, 1, 1);

        location.setX(location.getX() + 2);
        location.setZ(location.getZ() + 2);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                location.getWorld().spawnParticle(Particle.CRIT, location, 5);
                location.setX(location.getX() - 1);
            }
            location.setZ(location.getZ() - 1);
            location.setX(location.getX()+5);
        }
    }
}
