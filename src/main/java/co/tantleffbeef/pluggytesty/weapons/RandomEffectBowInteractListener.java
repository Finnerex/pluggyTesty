package co.tantleffbeef.pluggytesty.weapons;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;


public class RandomEffectBowInteractListener implements Listener {

    @EventHandler
    private void onEntityShootBow(EntityShootBowEvent event) {

        Entity entity = event.getEntity();

        if(!(entity instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        event.setConsumeItem(false);

        if (event.isCancelled())
            return;

        ItemMeta meta = event.getBow().getItemMeta();

        if (meta == null || meta.getLore() == null || !meta.getLore().get(0).equals(RandomEffectBow.REB_LORE))
            return;

        Arrow arrow1 = (Arrow) event.getProjectile();

        int arrow = new Random().nextInt(12);

        // Gavin and Finn are Gonna Get Mad At Me For Hard Coding
        if (arrow == 0)
            arrow1.addCustomEffect(PotionEffectType.BLINDNESS.createEffect(100, 1), false);
        if (arrow == 1)
            arrow1.addCustomEffect(PotionEffectType.HARM.createEffect(1, 1), false);
        if (arrow == 2)
            arrow1.addCustomEffect(PotionEffectType.HARM.createEffect(1, 2), false);
        if (arrow == 3)
            arrow1.addCustomEffect(PotionEffectType.POISON.createEffect(100, 1), false);
        if (arrow == 4)
            arrow1.addCustomEffect(PotionEffectType.POISON.createEffect(40, 2), false);
        if (arrow == 5)
            arrow1.addCustomEffect(PotionEffectType.SLOW.createEffect(60, 4), false);

        event.setProjectile(arrow1);
        player.getInventory().remove(Material.ARROW);
    }
}
