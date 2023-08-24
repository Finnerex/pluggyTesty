package co.tantleffbeef.pluggytesty.extra_listeners.custom_items;

import org.bukkit.Material;
import org.bukkit.MusicInstrument;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MusicInstrumentMeta;
import org.bukkit.potion.PotionEffectType;


public class GoatHornInteractListener implements Listener {

    private final static int EFFECT_DURATION_TICKS = 600; // 30 secs

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.GOAT_HORN || player.hasCooldown(Material.GOAT_HORN)
                || (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK))
            return;

        MusicInstrumentMeta meta = ((MusicInstrumentMeta) item.getItemMeta());
        assert meta != null;

        MusicInstrument instrument = meta.getInstrument();
        assert instrument != null;

        String instrumentString = instrument.getKey().getKey();

        switch (instrumentString.substring(0, instrumentString.indexOf("_goat_horn"))) {
            case "ponder" -> addEffect(player, PotionEffectType.JUMP, 3);
            case "admire" -> addEffect(player, PotionEffectType.FAST_DIGGING, 1);
            case "seek" -> addEffect(player, PotionEffectType.ABSORPTION, 4);
            case "call" -> addEffect(player, PotionEffectType.SPEED, 2);
            case "dream" -> addEffect(player, PotionEffectType.REGENERATION, 2);
            case "feel" -> addEffect(player, PotionEffectType.INCREASE_DAMAGE, 1);
            default -> {}
        }

    }

    private void addEffect(Player player, PotionEffectType type, int amplifier) {
        player.addPotionEffect(type.createEffect(EFFECT_DURATION_TICKS, amplifier));
        player.setCooldown(Material.GOAT_HORN, (int) (EFFECT_DURATION_TICKS * 1.4f));
    }
}
