package co.tantleffbeef.pluggytesty.misc;

import co.tantleffbeef.pluggytesty.PluggyTesty;
import co.tantleffbeef.pluggytesty.custom.item.armor.SimpleArmorItemType;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class RegisterAttributes {

    private final Plugin plugin;
    public RegisterAttributes(@NotNull Plugin plugin, String id, String name, Material material, Attribute attribute, String modName, int amount, EquipmentSlot slot){
        this.plugin = plugin;

        new SimpleArmorItemType(plugin, id, false, name, material,
                new SimpleArmorItemType.AttributePair(attribute, new AttributeModifier(UUID.randomUUID(), modName, amount, AttributeModifier.Operation.ADD_NUMBER, slot)),
                new SimpleArmorItemType.AttributePair(attribute, new AttributeModifier(UUID.randomUUID(), modName, amount, AttributeModifier.Operation.ADD_NUMBER, slot)));
    }
}
