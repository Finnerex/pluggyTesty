package co.tantleffbeef.pluggytesty.weapons;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RandomEffectBow implements CommandExecutor {

    public static final String REB_LORE = "Shoots Random Negative Effects at Target";

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player))
            return false;

        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta meta = bow.getItemMeta();

        meta.setDisplayName(ChatColor.GREEN + "Random Effect Bow");

        ArrayList<String> lore = new ArrayList<>();
        lore.add(REB_LORE);

        meta.setLore(lore);

        bow.setItemMeta(meta);
        player.getInventory().addItem(bow);
        player.updateInventory();

        return true;
    }

}
