package co.tantleffbeef.pluggytesty.custom.item.utility;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
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
        meta.setLore(List.of(org.bukkit.ChatColor.DARK_GREEN + "Right-Click : Thrust forward and upward"));
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {

        final int cooldown = player.getCooldown(Material.SOUL_CAMPFIRE);

        // im too lazy to make this good someone else can do it if they want
        final int fuel = 100 - cooldown;
        player.spigot ().sendMessage(ChatMessageType.ACTION_BAR,
                new ComponentBuilder("Fuel: [").color(ChatColor.GRAY)
                .append(String.valueOf(Math.max(0, fuel)))
                .color ((fuel > 50) ? ChatColor.GREEN : (fuel > 20) ? ChatColor.YELLOW : ChatColor.RED)
                .append("]").color(ChatColor.GRAY).create());

        if (cooldown >= 100) {
            if (cooldown < 110)
                player.setCooldown(Material.SOUL_CAMPFIRE, 140);

            return true;
        }

        player.setCooldown(Material.SOUL_CAMPFIRE, cooldown + 30);
        player.setVelocity(player.getLocation().getDirection().setY(1));

        return true;
    }
}
