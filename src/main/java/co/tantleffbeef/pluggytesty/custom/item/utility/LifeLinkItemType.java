package co.tantleffbeef.pluggytesty.custom.item.utility;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import co.tantleffbeef.pluggytesty.extra_listeners.custom_items.LifeLinkListener;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class LifeLinkItemType extends SimpleItemType implements InteractableItemType {

    public LifeLinkItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.WEEPING_VINES);
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);
        meta.setLore(Arrays.asList(ChatColor.DARK_GREEN + "Right-Click : Link to another player",
                ChatColor.DARK_GREEN + "Absorb 50% of linked player's taken damage (20 block range)",
                ChatColor.DARK_GREEN + "Sneak + Right-Click : Remove link"));
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {
        if (player.isSneaking()) {
            LifeLinkListener.resetPlayerLink(player.getUniqueId());
            player.sendMessage(ChatColor.GOLD + "Life link removed");
        }

        return true;
    }
}
