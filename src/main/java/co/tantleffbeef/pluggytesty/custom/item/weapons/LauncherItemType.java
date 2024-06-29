package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class LauncherItemType extends SimpleItemType implements InteractableItemType {

    private final Plugin schedulerPlugin;


    public LauncherItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.NETHER_BRICK);
        this.schedulerPlugin = namespace;
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);
        meta.setLore(Arrays.asList(ChatColor.DARK_GREEN + "Right-Click : Arced explosive", ChatColor.DARK_GREEN + "No Cooldown"));
    }

    public boolean interact(@NotNull Player player, @NotNull ItemStack item, Block block) {
        if (player.hasCooldown(Material.NETHER_BRICK))
            return false;

        BukkitRunnable runnable = new BukkitRunnable() {
            Location location = player.getLocation();
            int runs = 0;

            @Override
            public void run() {
                Block block = shootBolt(1, location);
                player.getWorld().spawnParticle(Particle.EFFECT, location, 1);

                if (block != null || runs > 10) {
                    if (block != null)
                        location = block.getLocation();

                    player.getWorld().createExplosion(location, 3);
                    cancel();
                    return;
                }

                float p = location.getPitch();
                location.setPitch(p + 5 + (p / -18));
                runs ++;
            }
        };

        runnable.runTaskTimer(schedulerPlugin, 0, 1);

        //world.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);

        player.setCooldown(Material.NETHER_BRICK, 5);

        return false;
    }

    private Block shootBolt(float range, Location location) {

        final World world = location.getWorld();

        Block block = null;
        RayTraceResult result = world.rayTraceBlocks(location, location.getDirection(), range);

        if (result != null)
            block = result.getHitBlock();

        for(float i = 0.1f; i < range; i += 0.1f) {
            location.add(location.getDirection().normalize().multiply(i));
            world.spawnParticle(Particle.EFFECT, location, 1);
        }

        return block;
    }
}
