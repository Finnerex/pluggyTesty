package co.tantleffbeef.pluggytesty;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class SwordsmansDream implements CommandExecutor {
    public static final String SWORD_LORE = "For those who wish to become the greatest swordsman.";

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;

        ItemStack sword = new ItemStack(Material.IRON_SWORD);

        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName("§bSpinny Boi");

        ArrayList<String> lore = new ArrayList<>();
        lore.add(SWORD_LORE);
        meta.setLore(lore);

        sword.setItemMeta(meta);
        player.getInventory().addItem(sword);
        player.updateInventory();

        return true;
    }
}
