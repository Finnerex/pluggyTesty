package co.tantleffbeef.pluggytesty;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Digga implements CommandExecutor {

    public static final String DIGGA_LORE = "digs";

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player))
            return false;

        Player player = (Player) commandSender;

        ItemStack ender = new ItemStack(Material.PRISMARINE_SHARD);
        ItemMeta metaEnder = ender.getItemMeta();

        metaEnder.setDisplayName("digga");

        ArrayList<String> lore = new ArrayList<>();
        lore.add(DIGGA_LORE);

        metaEnder.setLore(lore);
        ender.setItemMeta(metaEnder);

        player.damage(0.0000001);
        player.giveExpLevels(1);
        player.getInventory().addItem(ender);
        player.updateInventory();
        return true;
    }
}
