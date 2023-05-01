package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AxeOfYourMotherItemType extends SimpleItemType implements InteractableItemType {
    private final Plugin schedulerPlugin;

    public AxeOfYourMotherItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.DIAMOND_AXE);
        this.schedulerPlugin = namespace;
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {
        if (player.hasCooldown(Material.DIAMOND_AXE))
            return false;

        player.playSound(player, Sound.ENTITY_BAT_TAKEOFF, 1, 1);

        player.setCooldown(Material.DIAMOND_AXE, 300);

        dash(player);

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                Location location = player.getLocation().add(new Vector(0, -1, 0)).clone();
                if (!location.getBlock().getType().equals(Material.AIR)) {
                    applyAreaEffectDamage(player);
                    player.setFallDistance(0);
                    cancel();
                }
            }
        };
        runnable.runTaskTimer(schedulerPlugin, 10, 0);

        return false;
    }

    private void dash(Player player) {
        Location location = player.getLocation().clone();
        location.setPitch(-90);

        player.setVelocity(location.getDirection().normalize().multiply(2).add(player.getVelocity()));
    }

    private void applyAreaEffectDamage(@NotNull Player player) {
        Location location = player.getLocation();
        assert location.getWorld() != null; // Player should have a world I hope
        final var entities = location.getWorld().getNearbyEntities(location, 2.5, 2, 2.5);

        for (Entity entity : entities) {
            if (entity instanceof Damageable damageable && !entity.equals(player))
                damageable.damage(7, player);
        }

        location.getWorld().playSound(location, Sound.BLOCK_ANVIL_LAND, 1, 1);

        location.setX(location.getX() + 2);
        location.setZ(location.getZ() + 2);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                location.getWorld().spawnParticle(Particle.CRIT, location, 5);
                location.setX(location.getX() - 1);
            }
            location.setZ(location.getZ() - 1);
            location.setX(location.getX()+5);
        }
    }
}
