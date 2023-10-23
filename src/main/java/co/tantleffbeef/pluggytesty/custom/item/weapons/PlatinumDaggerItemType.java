package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlatinumDaggerItemType extends SimpleItemType {

    private final KeyManager<CustomNbtKey> keyManager;
    private final ResourceManager resourceManager;

    public PlatinumDaggerItemType(Plugin namespace, String id, boolean customModel, String name, KeyManager<CustomNbtKey> keyManager, ResourceManager resourceManager) {
        super(namespace, id, customModel, name, Material.IRON_SWORD);
        this.keyManager = keyManager;
        this.resourceManager = resourceManager;

        namespace.getServer().getPluginManager().registerEvents(new PlatinumDaggerAttackListener(), namespace);
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);
        meta.setUnbreakable(true);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED,
                new AttributeModifier(UUID.randomUUID(), "pluggyTesty", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,
                new AttributeModifier(UUID.randomUUID(), "pluggyTesty", 4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
    }

    private class PlatinumDaggerAttackListener implements Listener {

        @EventHandler
        public void onAttack(EntityDamageByEntityEvent event) {

            if (!(event.getEntity() instanceof LivingEntity target))
                return;

            if (!(event.getDamager() instanceof Player player))
                return;

            PlatinumDaggerItemType dagger = CustomItemType.asInstanceOf(PlatinumDaggerItemType.class, player.getInventory().getItemInMainHand(), keyManager, resourceManager);

            if (dagger == null)
                return;

            target.setNoDamageTicks(3);
        }

    }
}
