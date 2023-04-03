package co.tantleffbeef.pluggytesty;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class MagicStick implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        ItemStack stick = new ItemStack(Material.STICK);
        // custom name, description, make it enchanted
        stick.addEnchantment(Enchantment.RIPTIDE, 10);

        ItemMeta meta = stick.getItemMeta();
        // Change some stuff
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName("Magic Rod of Destiny!!!!!!111!");
        ArrayList<String> die = new ArrayList<String>();
        die.add("This stick has penetrated many villains over the years.");

        meta.setLore(die);
        stick.setItemMeta(meta);


        return true;
    }
}
