package co.tantleffbeef.pluggytesty.misc;

import co.tantleffbeef.pluggytesty.custom.item.armor.SimpleArmorItemType;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;

import java.util.UUID;

public class RegisterAttributes {
    public RegisterAttributes(String id, String name, Material material, Attribute attribute, String modName, int amount, EquipmentSlot slot){
        new SimpleArmorItemType(this, id, false, name, material, new SimpleArmorItemType.AttributePair(attribute, new AttributeModifier(UUID.randomUUID(), modName, amount, AttributeModifier.Operation.ADD_NUMBER, slot)), new SimpleArmorItemType());
    }
}
