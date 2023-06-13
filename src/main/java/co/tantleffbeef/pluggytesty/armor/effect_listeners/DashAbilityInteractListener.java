package co.tantleffbeef.pluggytesty.armor.effect_listeners;

import co.tantleffbeef.pluggytesty.armor.ArmorEffectType;
import co.tantleffbeef.pluggytesty.armor.ArmorEquipListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class DashAbilityInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getAction() != Action.LEFT_CLICK_AIR)
            return;

        if (event.getItem() != null)
            return;

        Player player = event.getPlayer();

        if (ArmorEquipListener.effectMap.get(player.getUniqueId()) != ArmorEffectType.DASH)
            return;

        // use the boots to hold the double click timer
        ItemStack boots = player.getInventory().getBoots();
        assert boots != null;

        if (!player.hasCooldown(boots.getType())) {
            player.setCooldown(boots.getType(), 10);
            return;
        }

        ItemStack cp = player.getInventory().getChestplate();
        assert cp != null;

        if (player.hasCooldown(cp.getType()))
            return;

        player.setVelocity(player.getVelocity().add(player.getEyeLocation().getDirection().multiply(2)));

        player.setCooldown(cp.getType(), 180);

    }

}
