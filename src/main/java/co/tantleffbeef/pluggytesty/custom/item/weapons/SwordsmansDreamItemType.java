package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class SwordsmansDreamItemType extends SimpleItemType implements InteractableItemType {

    private final Plugin schedulerPlugin;
    private final int COOLDOWN_TICKS = 20 * 8;

    public SwordsmansDreamItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.IRON_SWORD);
        this.schedulerPlugin = namespace;
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);
        meta.setLore(Arrays.asList(ChatColor.DARK_GREEN + "Shift + Right-Click : Fly though enemies, dealing area damage", ChatColor.DARK_GREEN + "Cooldown : " + COOLDOWN_TICKS / 20f + "s"));
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack item, Block block) {
        if (player.hasCooldown(Material.IRON_SWORD)) return false;

        final World world = player.getWorld();

        if(player.isSneaking()) {
            player.setCooldown(Material.IRON_SWORD, COOLDOWN_TICKS);
            world.playSound(player, Sound.BLOCK_GRINDSTONE_USE, 1, 1);
            player.setGravity(false);
            player.setVelocity(new Vector(0, 0.2, 0));

            BukkitRunnable runnable = new BukkitRunnable() {
                int tickNum = 0;

                @Override
                public void run() { // hi
                    tickNum++;
                    world.spawnParticle(Particle.SWEEP_ATTACK, player.getLocation(), 3, 1.0, 0.0, 1.0);
                    for (Entity i : player.getNearbyEntities(2, 2, 2)) {

                        if (i instanceof Damageable d)
                            d.damage(6, player);
                    }
                    if (tickNum >= 20) {
                        player.setVelocity(new Vector(0, 0, 0));
                    }
                    if (tickNum >= 30 && tickNum < 50) {
                        world.spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 1);
                        player.setVelocity(player.getLocation().getDirection());
                    }
                    if (tickNum >= 50) {
                        cancel();
                        player.setGravity(true);
                        player.setVelocity(player.getLocation().getDirection());

                    }
                }
            };

            runnable.runTaskTimer(schedulerPlugin, 0, 1);
        }

        return false;
    }
}
