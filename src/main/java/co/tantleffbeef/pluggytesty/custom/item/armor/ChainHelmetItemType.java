package co.tantleffbeef.pluggytesty.custom.item.armor;

import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.units.qual.A;

import java.util.UUID;

public class ChainHelmetItemType extends SimpleItemType {

    public ChainHelmetItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.CHAINMAIL_HELMET);
    }

    public static ItemStack Attributes() {

        ItemStack cH = new ItemStack(Material.CHAINMAIL_HELMET);
        ItemMeta cHMeta = cH.getItemMeta();

        cHMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "Armor", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));

        cH.setItemMeta(cHMeta);

        return cH;
    }
}











