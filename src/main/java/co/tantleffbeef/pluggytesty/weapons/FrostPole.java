package co.tantleffbeef.pluggytesty.weapons;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FrostPole implements CommandExecutor {
    public static final String POLE_LORE = "The frost pole is commonly used to cool down houses during summertime.";

    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        if (!(commandSender instanceof Player player)) return false;

        ItemStack pole = new ItemStack(Material.SOUL_TORCH);

        ItemMeta meta = pole.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName("§bThe Frost Pole");
        meta.addEnchant(Enchantment.FROST_WALKER, 2, true);

        ArrayList<String> lore = new ArrayList<>();
        lore.add(POLE_LORE);
        meta.setLore(lore);

        pole.setItemMeta(meta);
        player.getInventory().addItem(pole);
        player.updateInventory();

        return true;
    }
}