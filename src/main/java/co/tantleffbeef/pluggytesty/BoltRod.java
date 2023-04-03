package co.tantleffbeef.pluggytesty;

import com.google.common.collect.Multimap;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.joml.Random;

import javax.lang.model.element.Modifier;
import java.util.jar.Attributes;

public class BoltRod implements CommandExecutor {

    //public static final String ROD_LORE = "not stolen from hypixel sb";

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player))
            return false;

        Player player = (Player) commandSender;

        ItemStack rod = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = rod.getItemMeta();

        int rarityNum = new Random().nextInt(10);
        String rarity = rarityNum < 5 ? "§white Common" : (rarityNum < 8 ? "§blue Rare" : "§gold Legendary");

        meta.setDisplayName(rarity + "§white Bolt Rod");

        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier("generic.movementSpeed", rarityNum, AttributeModifier.Operation.ADD_NUMBER));

        rod.setItemMeta(meta);
        player.getInventory().addItem(rod);
        player.updateInventory();

        return true;
    }
}
