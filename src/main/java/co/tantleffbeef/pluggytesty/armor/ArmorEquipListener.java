package co.tantleffbeef.pluggytesty.armor;


import com.jeff_media.armorequipevent.ArmorEquipEvent;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
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

    // holds which players have which effects
    public static Map<UUID, ArmorEffectType> effectMap = new HashMap<>();

    // mapping each of the trim patterns to ArmorEffectTypes, so they can be used in switches and elsewhere
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
            entry(TrimPattern.WILD,     ArmorEffectType.KNOCKBACK_RESIST),
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

    // check the player's trim on armor change, respawn, join, and leave
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
        removeEffect(event.getPlayer());
    }

    private void afterArmorChange(Player player) {

        UUID playerUUID = player.getUniqueId();

        ItemStack[] armor = player.getInventory().getArmorContents();
        // check if same trims
        TrimPattern trim = sameTrim(armor);

        // remove the effect if not all trims are the same
        if (trim == null) {
            removeEffect(player);
            return;
        }

        // give the player the effect
        effectMap.put(playerUUID, trimMap.get(trim));

        switch (effectMap.get(playerUUID)) {
            case CONDUIT_POWER -> player.addPotionEffect(PotionEffectType.CONDUIT_POWER.createEffect(PotionEffect.INFINITE_DURATION, 0));
            case NIGHT_VISION -> player.addPotionEffect(PotionEffectType.NIGHT_VISION.createEffect(PotionEffect.INFINITE_DURATION, 0));
            case FIRE_RESISTANCE -> player.addPotionEffect(PotionEffectType.FIRE_RESISTANCE.createEffect(PotionEffect.INFINITE_DURATION, 0));
            case HEALTH_BOOST -> player.addPotionEffect(PotionEffectType.HEALTH_BOOST.createEffect(PotionEffect.INFINITE_DURATION, 4));
            case KNOCKBACK_RESIST -> player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.5f);
        }

        // Bukkit.broadcastMessage("effect: " + effectMap.get(playerUUID));

    }


    private TrimPattern sameTrim(ItemStack[] armor) {
        TrimPattern lastPattern = null;
        int i = 0;

        // check if all armor pieces have the same trim on

        do {

            ItemStack a = armor[i];

            if (a == null)
                return null;

            ArmorTrim trim = ((ArmorMeta) a.getItemMeta()).getTrim();

            if (trim == null)
                return null;

            TrimPattern pattern = trim.getPattern();

            if(i == 0) // first trim is null
                lastPattern = pattern;

            if (!pattern.equals(lastPattern))
                return null;

            lastPattern = pattern;

            i++;
        } while (i < armor.length);

        // return that trim
        return lastPattern;
    }

    private void removeEffect(Player player) {
        // remove potion effects and others from player
        UUID playerUUID = player.getUniqueId();

        ArmorEffectType effect = effectMap.get(playerUUID);

        if (effect != null) {

            switch (effect) {
                case CONDUIT_POWER -> player.removePotionEffect(PotionEffectType.CONDUIT_POWER);
                case NIGHT_VISION -> player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                case FIRE_RESISTANCE -> player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                case HEALTH_BOOST -> player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
                case KNOCKBACK_RESIST -> player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0);
            }
        }

        effectMap.remove(playerUUID);
    }


}
