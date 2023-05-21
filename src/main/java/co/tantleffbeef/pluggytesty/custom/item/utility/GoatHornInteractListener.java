package co.tantleffbeef.pluggytesty.custom.item.utility;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Set;

public class GoatHornInteractListener implements Listener {

    private final static int EFFECT_DURATION_TICKS = 600; // 30 secs

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.GOAT_HORN)
            return;

        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        Set<ItemFlag> flags = meta.getItemFlags();

//        List<String> lore = meta.getLore();
//        assert lore != null;

        Bukkit.broadcastMessage(flags.toString());

//        player.addPotionEffect(switch (lore.get(0)) {
//            case "Ponder" -> PotionEffectType.JUMP.createEffect(EFFECT_DURATION_TICKS, 4);
//            case "Admire" -> PotionEffectType.FAST_DIGGING.createEffect(EFFECT_DURATION_TICKS, 2);
//            case "Seek" -> PotionEffectType.ABSORPTION.createEffect(EFFECT_DURATION_TICKS, 10);
//            case "Call" -> PotionEffectType.SPEED.createEffect(EFFECT_DURATION_TICKS, 3);
//            case "Dream" -> PotionEffectType.REGENERATION.createEffect(EFFECT_DURATION_TICKS, 3);
//            case "Feel" -> PotionEffectType.INCREASE_DAMAGE.createEffect(EFFECT_DURATION_TICKS, 1);
//            default -> PotionEffectType.LUCK.createEffect(0, 0);
//        });
//
//        player.setCooldown(Material.GOAT_HORN, EFFECT_DURATION_TICKS * 2);

    }
}
