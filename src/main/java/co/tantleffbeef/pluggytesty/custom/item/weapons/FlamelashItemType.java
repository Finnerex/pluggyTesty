package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FlamelashItemType extends SimpleItemType implements InteractableItemType {

    private final Plugin schedulerPlugin;

    public FlamelashItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.ORANGE_CANDLE);
        this.schedulerPlugin = namespace;
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {
        if (player.hasCooldown(Material.ORANGE_CANDLE))
            return true;

        final Location location = player.getEyeLocation();

        Firework firework = player.getWorld().spawn(location, Firework.class, (f) -> {
            f.setShotAtAngle(true);
            f.getLocation().setDirection(location.getDirection().normalize());
            f.setBounce(true);
            f.setShooter(player);

            FireworkMeta meta = f.getFireworkMeta();

            meta.addEffect(buildFirework(FireworkEffect.Type.BURST));
            meta.addEffect(buildFirework(FireworkEffect.Type.BALL_LARGE));
            meta.addEffect(buildFirework(FireworkEffect.Type.BALL));
            meta.setPower(6);

            f.setFireworkMeta(meta);
        });

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isSneaking() || firework.isDetonated()) {
                    if(!firework.isDetonated())
                        firework.setVelocity(player.getEyeLocation().getDirection().multiply(2));
                    cancel();
                    return;
                }

                firework.setVelocity(player.getEyeLocation().getDirection());

            }
        };

        runnable.runTaskTimer(schedulerPlugin, 0, 2);

        return true;
    }

    private FireworkEffect buildFirework(FireworkEffect.Type type) {
        FireworkEffect.Builder builder = FireworkEffect.builder();

        builder.flicker(true);
        builder.trail(false);
        builder.withColor(Color.fromRGB(237, 150, 43));

        builder.with(type);

        return builder.build();
    }
}
