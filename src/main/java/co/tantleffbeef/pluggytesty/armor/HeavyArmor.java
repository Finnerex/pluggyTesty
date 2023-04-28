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

public class HeavyArmor implements CommandExecutor {

    public static final String HEAVY_LORE = "Caked Up";

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player))
            return false;

        ItemStack head = new ItemStack(Material.CHAINMAIL_HELMET);
        ItemMeta headMeta = head.getItemMeta();
        headMeta.setDisplayName("Heavy Helmet");
        ItemStack chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        ItemMeta chestMeta = chest.getItemMeta();
        chestMeta.setDisplayName("Heavy Chestplate");
        ItemStack pants = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        ItemMeta pantsMeta = pants.getItemMeta();
        pantsMeta.setDisplayName("Heavy Leggings");
        ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);
        ItemMeta bootsMeta = boots.getItemMeta();
        bootsMeta.setDisplayName("Heavy Boots");

        ArrayList<String> lore = new ArrayList<>();
        lore.add(HEAVY_LORE);

        ItemMeta[] armorMetas = {headMeta, chestMeta, pantsMeta, bootsMeta};
        ItemStack[] armorItems = {head, chest, pants, boots};


        for (int i = 0; i < armorMetas.length - 1; i++){

            armorMetas[i].setLore(lore);


            //armorMetas[i].addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier("ArmorHard", 3, AttributeModifier.Operation.ADD_NUMBER));
            //armorMetas[i].addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier("ArmorSlow", -0.015, AttributeModifier.Operation.ADD_NUMBER));
            armorMetas[i].addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier("Armor", 20, AttributeModifier.Operation.ADD_NUMBER));

            armorItems[i].setItemMeta(armorMetas[i]);
            player.getInventory().addItem(armorItems[i]);

        }

        player.updateInventory();

        return true;
    }

}
