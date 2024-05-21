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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class AxeOfYourMotherItemType extends SimpleItemType implements InteractableItemType {
    private final Plugin schedulerPlugin;
    private final int COOLDOWN_TICKS = 300;

    public static ArrayList<UUID> thangList = new ArrayList<UUID>();

    public AxeOfYourMotherItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.DIAMOND_AXE);
        this.schedulerPlugin = namespace;
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);
        meta.setLore(Arrays.asList(ChatColor.DARK_GREEN + "Right-Click : Ground pound ability", ChatColor.DARK_GREEN + "Cooldown : " + COOLDOWN_TICKS / 20f + "s"));
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {
        if (player.hasCooldown(Material.DIAMOND_AXE))
            return false;

        thangList.add(player.getUniqueId());

        player.getWorld().playSound(player, Sound.ENTITY_BAT_TAKEOFF, 1, 1);

        player.setCooldown(Material.DIAMOND_AXE, COOLDOWN_TICKS);

        dash(player);

        return false;
    }

    private void dash(Player player) {
        Location location = player.getLocation().clone();
        location.setPitch(-90);

        player.setVelocity(location.getDirection().normalize().multiply(2).add(player.getVelocity()));
    }
}
