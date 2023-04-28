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

public class AxeOfYourMother implements CommandExecutor {

    public static final String AOYB_LORE = "Launch Into The Air And Damage Enemies Why You Hit The Ground";

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player))
            return false;

        ItemStack axe = new ItemStack(Material.DIAMOND_AXE);
        ItemMeta meta = axe.getItemMeta();

        meta.setDisplayName(ChatColor.AQUA + "Axe of Your Mother");

        ArrayList<String> lore = new ArrayList<>();
        lore.add(AOYB_LORE);

        meta.setLore(lore);

        axe.setItemMeta(meta);
        player.getInventory().addItem(axe);
        player.updateInventory();

        return true;
    }
}
