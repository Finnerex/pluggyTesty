package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;


public class ClusterBombItemType extends SimpleItemType implements InteractableItemType {

    private final Plugin schedulerPlugin;
    private final int COOLDOWN_TICKS = 10;

    public ClusterBombItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.DIAMOND_AXE);
        this.schedulerPlugin = namespace;
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);
        meta.setLore(Arrays.asList(ChatColor.DARK_GREEN + "Right-Click : Projectile that explodes into arrows", ChatColor.DARK_GREEN + "Cooldown : " + COOLDOWN_TICKS / 20f + "s"));
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {

        if (player.hasCooldown(Material.COAL_BLOCK))
            return true;

        player.getWorld().playSound(player, Sound.ENTITY_ENDER_PEARL_THROW, 1, 1);

        player.setCooldown(Material.COAL_BLOCK, COOLDOWN_TICKS);

        Location location = player.getEyeLocation();

        EnderPearl pearl = player.getWorld().spawn(location, EnderPearl.class, (enderPearl) -> {
            enderPearl.setGravity(false);
            enderPearl.setVelocity(location.getDirection().normalize());
        });

        schedulerPlugin.getServer().getScheduler().runTaskLater(schedulerPlugin, () -> explode(pearl), 40);

        return true;
    }
    private void explode(EnderPearl pearl) {
        Location location = pearl.getLocation();


        pearl.remove();

        for (int i = 0; i < 8; i++) {
            location.getWorld().spawn(location, Arrow.class, (arrow) -> arrow.setVelocity(location.getDirection().normalize()));
            location.setYaw(location.getYaw() + 45);
        }

    }
}