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

public class MagicStick implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        ItemStack stick = new ItemStack(Material.STICK);
        // custom name, description, make it enchanted

        ItemMeta meta = stick.getItemMeta();
        // Change some stuff
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(player.getName() + "'s Magic Rod of Destiny!!!1!!!11!");
        meta.addEnchant(Enchantment.RIPTIDE, 10, true);

        ArrayList<String> die = new ArrayList<>();
        die.add("This stick has penetrated many villains over the years.");

        meta.setLore(die);

        stick.setItemMeta(meta);


        return true;
    }
}
