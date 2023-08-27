package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Random;

import java.util.Arrays;
import java.util.Collection;

public class BoombatStickItemType extends SimpleItemType implements InteractableItemType {

    private final int COOLDOWN_TICKS = 5;
    private final Plugin plugin;


    public BoombatStickItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.STICK);
        plugin = namespace;
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);

        meta.setLore(Arrays.asList(
                ChatColor.DARK_GREEN + "Right-Click : Shoot A Boombat",
                ChatColor.DARK_GREEN + "Cooldown : " + COOLDOWN_TICKS / 20f + "s",
                "Mr. Boombat Stick, Bomba Fantastic")
        );

    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack item, @Nullable Block targetBlock) {
        if (player.hasCooldown(Material.STICK))
            return false;

        Bat bat = player.getWorld().spawn(player.getEyeLocation(), Bat.class, (b) -> {
            b.setCustomName(ChatColor.DARK_PURPLE + "Boombat");
            b.setAware(false);
            b.setAware(true);
            b.setInvulnerable(true);
        });

        Random rand = new Random();

        BukkitRunnable runnable = new BukkitRunnable() {

            Vector direction = player.getEyeLocation().getDirection().add(
                    new Vector(rand.nextFloat() * 4 - 2, rand.nextFloat() * 4 - 2, rand.nextFloat() * 4 - 2)).normalize();

            @Override
            public void run() {
                Collection<Entity> entities = bat.getNearbyEntities(1, 1, 1);

                bat.teleport(bat.getLocation().add(direction));

                entities.forEach((entity) -> {
                    if (entity.getType() == EntityType.BAT)
                        entities.remove(entity);
                });

                entities.remove(player);

                if (!entities.isEmpty() || bat.getLocation().getBlock().getType() != Material.AIR) {
                    bat.getWorld().createExplosion(bat.getLocation(), 4, false, false, bat);
                    bat.remove();
                    cancel();
                    return;
                }

                if (bat.getLocation().distance(player.getLocation()) > 100)
                    cancel();

            }
        };

        runnable.runTaskTimer(plugin, 0, 0);

        player.setCooldown(Material.STICK, COOLDOWN_TICKS);

        return false;
    }

}
