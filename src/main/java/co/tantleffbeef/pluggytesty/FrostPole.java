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

public class FrostPole implements CommandExecutor {
    public static final String POLE_LORE1 = "The frost pole, while a decently powerful weapon, is also commonly";
    public static final String POLE_LORE2 = "used to cool down houses during particularly hot summer days.";

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }
}
