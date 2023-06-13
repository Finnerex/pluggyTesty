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
        player.getWorld().playSound(player, Sound.ENTITY_FISHING_BOBBER_RETRIEVE, 1, 1);

        player.setCooldown(cp.getType(), 90);

        BukkitRunnable runnable = new BukkitRunnable() {

            int runs = 0;
            @Override
            public void run() {

                String red = "|".repeat(90/3 - runs);
                String green = "|".repeat(runs);

                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        new ComponentBuilder(green).color(ChatColor.GREEN)
                        .append(red).color(ChatColor.RED).create());

                runs ++;
            }
        };

        runnable.runTaskTimer(plugin, 0, 3);

    }

}
