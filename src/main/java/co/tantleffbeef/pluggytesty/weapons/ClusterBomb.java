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

public class ClusterBomb implements CommandExecutor {

    public static final String CB_LORE = "ill do it later";

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player))
            return false;

        ItemStack proj = new ItemStack(Material.SNOWBALL);
        ItemMeta meta = proj.getItemMeta();

        meta.setDisplayName(ChatColor.RED + "Cluster Bomb");


        ArrayList<String> lore = new ArrayList<>();
        lore.add(CB_LORE);

        meta.setLore(lore);

        proj.setItemMeta(meta);
        player.getInventory().addItem(proj);
        player.updateInventory();

        return true;
    }
}
