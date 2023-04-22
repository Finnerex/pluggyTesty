package co.tantleffbeef.pluggytesty.armor;


import com.jeff_media.armorequipevent.ArmorEquipEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import static java.util.Map.entry;

public class ArmorEquipListener implements Listener {

    private final Plugin plugin;
    public static Map<UUID, ArmorEffectType> effectMap = new HashMap<>();

    private final static Map<TrimPattern, ArmorEffectType> trimMap = Map.ofEntries(

            entry(TrimPattern.COAST,    ArmorEffectType.CONDUIT_POWER),
//            entry(TrimPattern.HOST,     ArmorEffectType.DEBUFF_DAMAGE_IMMUNITY),
            entry(TrimPattern.SENTRY,   ArmorEffectType.ARROW_CONSERVATION),
            entry(TrimPattern.EYE,      ArmorEffectType.NIGHT_VISION),
//            entry(TrimPattern.RAISER,   ArmorEffectType.JUMP_BOOST),
//            entry(TrimPattern.SHAPER,   ArmorEffectType.HASTE),
//            entry(TrimPattern.WAYFINDER,ArmorEffectType.SPEED),
            entry(TrimPattern.TIDE,     ArmorEffectType.EXP_BOOST),
            entry(TrimPattern.SNOUT,    ArmorEffectType.FIRE_RESISTANCE),
            entry(TrimPattern.WILD,     ArmorEffectType.HUNGER_CONSERVATION),
            entry(TrimPattern.VEX,      ArmorEffectType.DAMAGE_INCREASE),
            entry(TrimPattern.SPIRE,    ArmorEffectType.FALL_DAMAGE_IMMUNITY),
            entry(TrimPattern.RIB,      ArmorEffectType.WITHER_ATTACKS),
//            entry(TrimPattern.SILENCE,  ArmorEffectType.DASH),
            entry(TrimPattern.WARD,     ArmorEffectType.HEALTH_BOOST),
            entry(TrimPattern.DUNE,     ArmorEffectType.REGEN_ON_KILL)

    );

    public ArmorEquipListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onArmorChange(ArmorEquipEvent event) {
        plugin.getServer().getScheduler().runTask(plugin, () -> afterArmorChange(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        afterArmorChange(event.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        plugin.getServer().getScheduler().runTask(plugin, () -> afterArmorChange(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        ArmorEffectType effect = effectMap.get(playerUUID);

        if (effect != null) {

            switch (effect) {
                case CONDUIT_POWER -> player.removePotionEffect(PotionEffectType.CONDUIT_POWER);
                case NIGHT_VISION -> player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                case FIRE_RESISTANCE -> player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                case HEALTH_BOOST -> player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
            }
        }

        effectMap.remove(playerUUID);

    }

    private void afterArmorChange(Player player) {
//        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        ItemStack[] armor = player.getInventory().getArmorContents();

        TrimPattern trim = sameTrim(armor);

        if (trim == null) {
            ArmorEffectType effect = effectMap.get(playerUUID);

            if (effect != null) {

                switch (effect) {
                    case CONDUIT_POWER -> player.removePotionEffect(PotionEffectType.CONDUIT_POWER);
                    case NIGHT_VISION -> player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    case FIRE_RESISTANCE -> player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                    case HEALTH_BOOST -> player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
                }
            }

            effectMap.remove(playerUUID);
            return;
        }

        effectMap.put(playerUUID, trimMap.get(trim));

        switch (effectMap.get(playerUUID)) {
            case CONDUIT_POWER -> player.addPotionEffect(PotionEffectType.CONDUIT_POWER.createEffect(PotionEffect.INFINITE_DURATION, 0));
            case NIGHT_VISION -> player.addPotionEffect(PotionEffectType.NIGHT_VISION.createEffect(PotionEffect.INFINITE_DURATION, 0));
            case FIRE_RESISTANCE -> player.addPotionEffect(PotionEffectType.FIRE_RESISTANCE.createEffect(PotionEffect.INFINITE_DURATION, 0));
            case HEALTH_BOOST -> player.addPotionEffect(PotionEffectType.HEALTH_BOOST.createEffect(PotionEffect.INFINITE_DURATION, 4));
        }

        Bukkit.broadcastMessage("effect: " + effectMap.get(playerUUID));

    }

    private TrimPattern sameTrim(ItemStack[] armor) {

        ItemStack last = armor[0];

        if (last == null)
            return null;

        ArmorMeta meta = (ArmorMeta) last.getItemMeta();

        if (meta == null)
            return null;

        ArmorTrim trim = meta.getTrim();

        if (trim == null)
            return null;

        TrimPattern lastPattern = trim.getPattern();

        for (int i = 1; i < armor.length; i++) {
            ItemStack a = armor[i];

            if (a == null)
                return null;

            meta = (ArmorMeta) a.getItemMeta();

            if (meta == null)
                return null;

            trim = meta.getTrim();

            if (trim == null)
                return null;

            TrimPattern pattern = trim.getPattern();

            if (!pattern.equals(lastPattern))
                return null;

            lastPattern = pattern;

        }

        return lastPattern;
    }

}
