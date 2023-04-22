package co.tantleffbeef.pluggytesty.weapons;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.joml.Random;

import java.util.ArrayList;

public class BoltRod implements CommandExecutor {

    public static final String ROD_LORE = "not stolen from hypixel sb";

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        if (!(commandSender instanceof Player player))
            return false;

        ItemStack rod = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = rod.getItemMeta();

        int rarityNum = new Random().nextInt(10);
        String rarity = rarityNum < 5 ? "§7Common" : (rarityNum < 8 ? "§9Rare" : "§6§lLegendary");
        float speedBoost = rarity.equals("§7Common") ? 0f : (rarity.equals("§9Rare") ? 0.1f : 0.2f);

        meta.setDisplayName(rarity + " §eBolt Rod");

        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier("generic.movementSpeed", speedBoost, AttributeModifier.Operation.ADD_NUMBER));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ROD_LORE);

        meta.setLore(lore);

        rod.setItemMeta(meta);
        player.getInventory().addItem(rod);
        player.updateInventory();

        return true;
    }

}
