package co.tantleffbeef.pluggytesty.utility;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HealingHeart implements CommandExecutor {

    public static final String HEART_LORE = "hold to charge heal";
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(commandSender instanceof Player player))
            return false;

        ItemStack item = new ItemStack(Material.REDSTONE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.DARK_RED + "Healing Heart");

        ArrayList<String> lore = new ArrayList<>();
        lore.add(HEART_LORE);

        meta.setLore(lore);
        item.setItemMeta(meta);

        player.getInventory().addItem(item);
        player.updateInventory();

        return true;
    }
}