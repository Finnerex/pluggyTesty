package co.tantleffbeef.pluggytesty;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Goer implements CommandExecutor {

    public static final String GOER_LORE = "makes you go";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return false;

        Player player = (Player) sender;

        ItemStack item = new ItemStack(Material.LEATHER);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Go!");

        ArrayList<String> lore = new ArrayList<>();
        lore.add(GOER_LORE);

        meta.setLore(lore);

        item.setItemMeta(meta);

        player.sendMessage("??");

        player.getInventory().addItem(item);
        player.updateInventory();

        return true;

    }
}
