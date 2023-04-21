package co.tantleffbeef.pluggytesty.weapons;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Launcher implements CommandExecutor {

    public static final String LAUNCH_LORE = "launcher";
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        ItemStack pole = new ItemStack(Material.NETHER_BRICK);

        ItemMeta meta = pole.getItemMeta();
        meta.setDisplayName("Launcher");

        ArrayList<String> lore = new ArrayList<>();
        lore.add(LAUNCH_LORE);
        meta.setLore(lore);

        pole.setItemMeta(meta);
        player.getInventory().addItem(pole);
        player.updateInventory();

        return true;
    }
}
