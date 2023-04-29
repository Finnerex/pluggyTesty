package co.tantleffbeef.pluggytesty.armor.effect_listeners;

import co.tantleffbeef.pluggytesty.armor.ArmorEffectType;
import co.tantleffbeef.pluggytesty.armor.ArmorEquipListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;

public class EntityEffectListener implements Listener {

    private final Plugin plugin;

    public EntityEffectListener(Plugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void onEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;

        if (ArmorEquipListener.effectMap.get(player.getUniqueId()) != ArmorEffectType.NIGHT_VISION)
            return;

        PotionEffectType effect = event.getNewEffect().getType();

        if (effect.equals(PotionEffectType.BLINDNESS))
            plugin.getServer().getScheduler().runTask(plugin, () -> player.removePotionEffect(PotionEffectType.BLINDNESS));

        if (effect.equals(PotionEffectType.DARKNESS))
            plugin.getServer().getScheduler().runTask(plugin, () -> player.removePotionEffect(PotionEffectType.DARKNESS));

    }

}
