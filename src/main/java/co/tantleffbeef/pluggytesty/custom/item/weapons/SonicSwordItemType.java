package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SonicSwordItemType extends SimpleItemType implements InteractableItemType {

    private Plugin schedulerPlugin;

    public SonicSwordItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.STONE_SWORD);
        this.schedulerPlugin = namespace;
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {

        if (player.hasCooldown(Material.STONE_SWORD))
            return false;

        final World w = player.getWorld();


        BukkitRunnable runnable = new BukkitRunnable() {

            final Location l = player.getEyeLocation().clone();
            final Vector d = l.getDirection();

            int distance = 1;
            @Override
            public void run() {
                if (distance > 10) {
                    cancel();
                    return;
                }


                for (int i = -1; i < 2; i += 2) {

                    final Location l2 = l.clone();
                    final Vector pd = d.clone().setY(0).rotateAroundY(90);

                    for (int j = 0; j < distance * 2; j++) {
                        w.spawnParticle(Particle.SPELL_INSTANT, l2, 1);
                        l2.add(pd.multiply(i));
                        pd.rotateAroundY(5 * i);
                    }
                }

                l.add(d);

                distance ++;

            }
        };

        runnable.runTaskTimer(schedulerPlugin, 0, 0);

        return false;
    }
}
