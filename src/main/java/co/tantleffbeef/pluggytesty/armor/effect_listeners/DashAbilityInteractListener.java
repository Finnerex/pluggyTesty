package co.tantleffbeef.pluggytesty.armor.effect_listeners;

import co.tantleffbeef.pluggytesty.armor.ArmorEffectType;
import co.tantleffbeef.pluggytesty.armor.ArmorEquipListener;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DashAbilityInteractListener implements Listener {

    private final Plugin plugin;
    private final int DASH_CD_TICKS = 45;

    public DashAbilityInteractListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {


        if (event.getAction() != Action.LEFT_CLICK_AIR)
            return;

        if (event.getItem() != null)
            return;

        Player player = event.getPlayer();

        if (ArmorEquipListener.effectMap.get(player.getUniqueId()) != ArmorEffectType.DASH)
            return;

        // use the boots to hold the double click timer OMG EPIC COMMENT
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
        player.getWorld().playSound(player, Sound.ENTITY_FISHING_BOBBER_RETRIEVE, 1, 1);

        player.setCooldown(cp.getType(), DASH_CD_TICKS);

        BukkitRunnable runnable = new BukkitRunnable() {

            int runs = 0;
            @Override
            public void run() {

                if (runs > DASH_CD_TICKS / 5) {
                    cancel();
                    return;
                }

                String red = "|".repeat(DASH_CD_TICKS/5 - runs);
                String green = "|".repeat(runs);

                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        new ComponentBuilder("Dash: [").color(ChatColor.GRAY)
                        .append(green).color(ChatColor.GREEN)
                        .append(red).color(ChatColor.RED)
                        .append("]").color(ChatColor.GRAY).create());

                runs ++;
            }
        };

        runnable.runTaskTimer(plugin, 0, 5);

    }

}
