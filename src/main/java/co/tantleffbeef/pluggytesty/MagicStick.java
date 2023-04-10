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
    public static final String STICK_LORE = "This stick has penetrated many villains over the years.";
    private static int number = 1;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player))
            return false;


        Player player = (Player) commandSender;

        ItemStack stick = new ItemStack(Material.STICK);
        // custom name, description, make it enchanted

        ItemMeta meta = stick.getItemMeta();
        // Change some stuff
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(player.getName() + "'s Magic Rod of Destiny!!!1!!!11!");
        meta.addEnchant(Enchantment.RIPTIDE, 1, true);

        ArrayList<String> lore = new ArrayList<>();
        lore.add(STICK_LORE);
        lore.add("Edition #" + number);
        number++;

        meta.setLore(lore);

        stick.setItemMeta(meta);

        player.getInventory().addItem(stick);
        player.updateInventory();

        return true;
    }

}
