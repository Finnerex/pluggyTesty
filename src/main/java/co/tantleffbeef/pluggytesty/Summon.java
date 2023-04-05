package co.tantleffbeef.pluggytesty;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Summon implements CommandExecutor {

    public static final String SUMMON_LORE = "summons zombies fr";
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return false;

        Player player = (Player) sender;

        ItemStack item = new ItemStack(Material.ROTTEN_FLESH);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Staff Of The Undead");

        ArrayList<String> lore = new ArrayList<>();
        lore.add(SUMMON_LORE);

        meta.setLore(lore);
        item.setItemMeta(meta);

        player.getInventory().addItem(item);
        player.updateInventory();

        return true;
    }
}
