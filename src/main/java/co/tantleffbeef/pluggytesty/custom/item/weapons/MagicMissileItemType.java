package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Random;

public class MagicMissileItemType extends SimpleItemType implements InteractableItemType {

    private final Plugin schedulerPlugin;
    private final int COOLDOWN_TICKS = 15;

    public MagicMissileItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.BLUE_CANDLE);
        this.schedulerPlugin = namespace;
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);
        meta.setLore(Arrays.asList(ChatColor.DARK_GREEN + "Shift + Right-Click : Guidable rocket", ChatColor.DARK_GREEN + "Cooldown : " + COOLDOWN_TICKS / 20f + "s"));
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {
        if (player.hasCooldown(Material.BLUE_CANDLE))
            return true;

        final Location location = player.getEyeLocation();

        // create a new firework with 3 effects (fewer for less dmg)
        Firework firework = player.getWorld().spawn(location, Firework.class, (f) -> {
            f.setShotAtAngle(true);
            f.getLocation().setDirection(location.getDirection().normalize());
            f.setBounce(true);
            f.setShooter(player);

            FireworkMeta meta = f.getFireworkMeta();

            meta.addEffect(buildFirework(FireworkEffect.Type.BURST));
            meta.addEffect(buildFirework(FireworkEffect.Type.BALL_LARGE));
            meta.addEffect(buildFirework(FireworkEffect.Type.BALL));
            meta.setPower(4);

            f.setFireworkMeta(meta);
        });

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                // end if explodes or player stops sneaking
                if (!player.isSneaking() || firework.isDetonated()) {
                    if(!firework.isDetonated())
                        firework.setVelocity(player.getEyeLocation().getDirection().multiply(2));
                    cancel();
                    return;
                }

                // follow player turning
                firework.setVelocity(player.getEyeLocation().getDirection());

            }
        };

        runnable.runTaskTimer(schedulerPlugin, 0, 1);

        player.setCooldown(Material.BLUE_CANDLE, COOLDOWN_TICKS);

        return true;
    }

    private FireworkEffect buildFirework(FireworkEffect.Type type) {
        FireworkEffect.Builder builder = FireworkEffect.builder();

        final int color = new Random().nextInt(100);

        builder.flicker(true);
        builder.trail(false);
        // different shades of blue
        builder.withColor(Color.fromRGB(40 , 50 + color, 220));

        builder.with(type);

        return builder.build();
    }
}
