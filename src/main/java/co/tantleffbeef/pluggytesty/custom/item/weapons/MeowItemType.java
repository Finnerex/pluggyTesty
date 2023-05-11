package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MeowItemType extends SimpleItemType implements InteractableItemType {

    private final Plugin schedulerPlugin;

    public MeowItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.NETHERITE_SWORD);
        this.schedulerPlugin = namespace;
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {


        LivingEntity Projectile = player.getWorld().spawn(player.getEyeLocation(), Slime.class, (proj) -> {
            proj.setSize(5);
            proj.setInvulnerable(true);
            proj.setGravity(true);
        });


        BukkitRunnable runnable = new BukkitRunnable() {
            int tick = 0;
            @Override
            public void run() {

                if (tick == 15){
                    cancel();
                    return;
                }
                else{
                    tick++;
                }

               Projectile.setVelocity(player.getEyeLocation().getDirection());

            }
        };
        runnable.runTaskTimer(schedulerPlugin, 0, 0);


        return true;
    }

}
