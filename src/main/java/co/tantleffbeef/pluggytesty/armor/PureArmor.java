package co.tantleffbeef.pluggytesty.armor;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public abstract class PureArmor {

    public static ItemStack lH(){
        ItemStack Item = new ItemStack(Material.LEATHER_HELMET);
        ItemMeta Meta = Item.getItemMeta();
        Meta.setDisplayName("Pure Leather Helmet");
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "armor", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));
        Item.setItemMeta(Meta);

        return Item;
    }

    public static ItemStack lC(){
        ItemStack Item = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemMeta Meta = Item.getItemMeta();
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
        Item.setItemMeta(Meta);

        return Item;
    }
    public static ItemStack lL(){
        ItemStack Item = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemMeta Meta = Item.getItemMeta();
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));
        Item.setItemMeta(Meta);

        return Item;
    }
    public static ItemStack lB(){
        ItemStack Item = new ItemStack(Material.LEATHER_BOOTS);
        ItemMeta Meta = Item.getItemMeta();
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET));
        Item.setItemMeta(Meta);

        return Item;
    }
    public static ItemStack cH(){
        ItemStack Item = new ItemStack(Material.CHAINMAIL_HELMET);
        ItemMeta Meta = Item.getItemMeta();
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));
        Item.setItemMeta(Meta);

        return Item;
    }
    public static ItemStack cC(){
        ItemStack Item = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        ItemMeta Meta = Item.getItemMeta();
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
        Item.setItemMeta(Meta);

        return Item;
    }
    public static ItemStack cL(){
        ItemStack Item = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        ItemMeta Meta = Item.getItemMeta();
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));
        Item.setItemMeta(Meta);

        return Item;
    }
    public static ItemStack cB(){
        ItemStack Item = new ItemStack(Material.CHAINMAIL_BOOTS);
        ItemMeta Meta = Item.getItemMeta();
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET));
        Item.setItemMeta(Meta);

        return Item;
    }
    public static ItemStack iH(){
        ItemStack Item = new ItemStack(Material.IRON_HELMET);
        ItemMeta Meta = Item.getItemMeta();
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));
        Item.setItemMeta(Meta);

        return Item;
    }
    public static ItemStack iC(){
        ItemStack Item = new ItemStack(Material.IRON_CHESTPLATE);
        ItemMeta Meta = Item.getItemMeta();
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
        Item.setItemMeta(Meta);

        return Item;
    }
    public static ItemStack iL(){
        ItemStack Item = new ItemStack(Material.IRON_LEGGINGS);
        ItemMeta Meta = Item.getItemMeta();
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));
        Item.setItemMeta(Meta);

        return Item;
    }
    public static ItemStack iB(){
        ItemStack Item = new ItemStack(Material.IRON_BOOTS);
        ItemMeta Meta = Item.getItemMeta();
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET));
        Item.setItemMeta(Meta);

        return Item;
    }
    public static ItemStack gH(){
        ItemStack Item = new ItemStack(Material.GOLDEN_HELMET);
        ItemMeta Meta = Item.getItemMeta();
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "armor", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));
        Item.setItemMeta(Meta);

        return Item;
    }
    public static ItemStack gC(){
        ItemStack Item = new ItemStack(Material.GOLDEN_CHESTPLATE);
        ItemMeta Meta = Item.getItemMeta();
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "armor", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
        Item.setItemMeta(Meta);

        return Item;
    }
    public static ItemStack gL(){
        ItemStack Item = new ItemStack(Material.GOLDEN_LEGGINGS);
        ItemMeta Meta = Item.getItemMeta();
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "armor", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));
        Item.setItemMeta(Meta);

        return Item;
    }
    public static ItemStack gB(){
        ItemStack Item = new ItemStack(Material.GOLDEN_BOOTS);
        ItemMeta Meta = Item.getItemMeta();
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET));
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "armor", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET));
        Item.setItemMeta(Meta);

        return Item;
    }
    public static ItemStack dH(){
        ItemStack Item = new ItemStack(Material.DIAMOND_HELMET);
        ItemMeta Meta = Item.getItemMeta();
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "armor", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));
        Item.setItemMeta(Meta);

        return Item;
    }
    public static ItemStack dC(){
        ItemStack Item = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemMeta Meta = Item.getItemMeta();
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "armor", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
        Item.setItemMeta(Meta);

        return Item;
    }
    public static ItemStack dL(){
        ItemStack Item = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemMeta Meta = Item.getItemMeta();
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "armor", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));
        Item.setItemMeta(Meta);

        return Item;
    }
    public static ItemStack dB(){
        ItemStack Item = new ItemStack(Material.DIAMOND_BOOTS);
        ItemMeta Meta = Item.getItemMeta();
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET));
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "armor", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET));
        Item.setItemMeta(Meta);

        return Item;
    }
    public static ItemStack nH(){
        ItemStack Item = new ItemStack(Material.NETHERITE_BOOTS);
        ItemMeta Meta = Item.getItemMeta();
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "armor", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));
        Item.setItemMeta(Meta);

        return Item;
    }
    public static ItemStack nC(){
        ItemStack Item = new ItemStack(Material.NETHERITE_CHESTPLATE);
        ItemMeta Meta = Item.getItemMeta();
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "armor", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
        Item.setItemMeta(Meta);

        return Item;
    }
    public static ItemStack nL(){
        ItemStack Item = new ItemStack(Material.NETHERITE_LEGGINGS);
        ItemMeta Meta = Item.getItemMeta();
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "armor", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));
        Item.setItemMeta(Meta);

        return Item;
    }
    public static ItemStack nB(){
        ItemStack Item = new ItemStack(Material.NETHERITE_BOOTS);
        ItemMeta Meta = Item.getItemMeta();
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "armor", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET));
        Meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "armor", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET));
        Item.setItemMeta(Meta);

        return Item;
    }


}
