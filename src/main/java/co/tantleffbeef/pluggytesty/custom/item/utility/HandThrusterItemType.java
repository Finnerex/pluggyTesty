package co.tantleffbeef.pluggytesty.custom.item.utility;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HandThrusterItemType extends SimpleItemType implements InteractableItemType {
    public HandThrusterItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.SOUL_CAMPFIRE);
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);
        meta.setLore(List.of(ChatColor.DARK_GREEN + "Hold Right-Click: Thrust forward and upward"));
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {
        if (player.getCooldown(Material.SOUL_CAMPFIRE) >= 60)
            return true;

        player.setCooldown(Material.SOUL_CAMPFIRE, player.getCooldown(Material.SOUL_CAMPFIRE) + 1);

        player.setVelocity(player.getLocation().getDirection().setY(2));

        return true;
    }
}
