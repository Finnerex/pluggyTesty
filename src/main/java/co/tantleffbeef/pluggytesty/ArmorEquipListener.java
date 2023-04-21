package co.tantleffbeef.pluggytesty;


import com.jeff_media.armorequipevent.ArmorEquipEvent;
import com.jeff_media.armorequipevent.ArmorType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.security.cert.TrustAnchor;
import java.util.ArrayList;

public class ArmorEquipListener implements Listener {

    private final Plugin plugin;

    public ArmorEquipListener(Plugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void onArmorChange(ArmorEquipEvent event) {
        Bukkit.broadcastMessage("InventoryInteractEvent");

        plugin.getServer().getScheduler().runTask(plugin, () -> afterArmorChange(event));

    }

    private void afterArmorChange(ArmorEquipEvent event) {
        Player player = event.getPlayer();

        ItemStack piece = event.getNewArmorPiece();

        ItemStack[] armor = player.getInventory().getArmorContents();

        TrimPattern pattern = sameTrim(armor);

        if (pattern == null) {

            ArrayList<PotionEffect> effects = (ArrayList<PotionEffect>) player.getActivePotionEffects();

            for (PotionEffect e : effects) {
                player.removePotionEffect(e.getType());
            } // removes all them dawgone dawgs which idk man

            return;
        }

        Bukkit.broadcastMessage("pattern not null");
        if (pattern.equals(TrimPattern.COAST))
            player.addPotionEffect(PotionEffectType.CONDUIT_POWER.createEffect(PotionEffect.INFINITE_DURATION, 4));
        else if (pattern.equals(TrimPattern.EYE))
            player.addPotionEffect(PotionEffectType.NIGHT_VISION.createEffect(PotionEffect.INFINITE_DURATION, 4));
//        else if (pattern.equals(TrimPattern.R))

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
