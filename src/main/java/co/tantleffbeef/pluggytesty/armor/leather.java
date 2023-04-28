package co.tantleffbeef.pluggytesty.armor;

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

import java.util.ArrayList;

public class leather implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player))
            return false;

        ItemStack head = new ItemStack(Material.LEATHER_HELMET);
        ItemMeta headMeta = head.getItemMeta();
        headMeta.setDisplayName("Helmet");
        ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemMeta chestMeta = chest.getItemMeta();
        chestMeta.setDisplayName("Chestplate");
        ItemStack pants = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemMeta pantsMeta = pants.getItemMeta();
        pantsMeta.setDisplayName("Leggings");
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        ItemMeta bootsMeta = boots.getItemMeta();
        bootsMeta.setDisplayName("Boots");


        ItemMeta[] HBMetas = {headMeta, bootsMeta};
        ItemMeta[] CLMetas = {chestMeta, pantsMeta};
        ItemStack[] HB = {head, chest, pants, boots};
        ItemStack[] CL = {head, chest, pants, boots};


        for (int i = 0; i < HBMetas.length; i++){


            HBMetas[i].addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier("ArmorHard", 0, AttributeModifier.Operation.ADD_NUMBER));
            HBMetas[i].addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier("Armor", 1, AttributeModifier.Operation.ADD_NUMBER));

            HB[i].setItemMeta(HBMetas[i]);
            player.getInventory().addItem(HB[i]);

        }
        for (int i = 0; i < CLMetas.length; i++){


            CLMetas[i].addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier("ArmorHard", 0, AttributeModifier.Operation.ADD_NUMBER));
            CLMetas[i].addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier("Armor", 2, AttributeModifier.Operation.ADD_NUMBER));

            CL[i].setItemMeta(CLMetas[i]);
            player.getInventory().addItem(CL[i]);

        }

        player.updateInventory();

        return true;
    }

}