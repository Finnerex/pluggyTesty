package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

public class BoltRodItemType extends SimpleItemType implements InteractableItemType {

    private final int COOLDOWN_TICKS = 5;

    public BoltRodItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.BLAZE_ROD);
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);

        meta.setLore(Arrays.asList(ChatColor.DARK_GREEN + "Right-Click : Bolt of energy", ChatColor.DARK_GREEN + "Cooldown : " + COOLDOWN_TICKS / 20f));

        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(),"generic.movementSpeed", 0.1f, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack item, Block block) {
        if (player.hasCooldown(Material.BLAZE_ROD))
            return false;

        Damageable hitEntity = (Damageable) shootBolt(3.5f, player.getEyeLocation());
        if (hitEntity != null)
            hitEntity.damage(3, player);

        player.getWorld().playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);

        player.setCooldown(Material.BLAZE_ROD, COOLDOWN_TICKS);

        return false;
    }

    private Entity shootBolt(float range, Location location) {

        location.add(location.getDirection().multiply(2));
        final World world = location.getWorld();

        Entity entity = null;
        RayTraceResult result = world.rayTraceEntities(location, location.getDirection(), range * 10);

        if (result != null)
            entity = result.getHitEntity();

        if (!(entity instanceof Damageable))
            entity = null;

        for(float i = 0.1f; i < range; i += 0.1f) {
            location.add(location.getDirection().multiply(i));
            world.spawnParticle(Particle.SPELL_INSTANT, location, 1);
        }

        return entity;
    }


}
