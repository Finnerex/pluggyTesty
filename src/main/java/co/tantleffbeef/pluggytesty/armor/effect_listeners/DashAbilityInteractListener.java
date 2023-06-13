package co.tantleffbeef.pluggytesty.armor.effect_listeners;

import co.tantleffbeef.pluggytesty.armor.ArmorEffectType;
import co.tantleffbeef.pluggytesty.armor.ArmorEquipListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class DashAbilityInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (event.getItem() != null)
            return;

        Player player = event.getPlayer();

        if (ArmorEquipListener.effectMap.get(player.getUniqueId()) != ArmorEffectType.DASH)
            return;

        ItemStack cp = player.getInventory().getChestplate();
        assert cp != null;

        if (player.hasCooldown(cp.getType()))
            return;

        player.setVelocity(player.getVelocity().add(player.getEyeLocation().getDirection().multiply(2)));

        player.setCooldown(cp.getType(), 180);

    }

}
