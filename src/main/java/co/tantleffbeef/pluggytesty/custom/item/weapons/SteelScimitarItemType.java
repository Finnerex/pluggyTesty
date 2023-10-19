package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SteelScimitarItemType extends SimpleItemType {

    private final KeyManager<CustomNbtKey> keyManager;
    private final ResourceManager resourceManager;

    private final Map<UUID, EntityDamageByEntityEvent> ignoredEvents;

    private final Plugin plugin;

    public SteelScimitarItemType(Plugin namespace, String id, boolean customModel, String name, KeyManager<CustomNbtKey> keyManager, ResourceManager resourceManager) {
        super(namespace, id, customModel, name, Material.STONE_SWORD);
        this.keyManager = keyManager;
        this.resourceManager = resourceManager;
        ignoredEvents = new HashMap<>();
        plugin = namespace;

        namespace.getServer().getPluginManager().registerEvents(new SteelScimitarAttackListener(), namespace);
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);
        meta.setUnbreakable(true);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier("Attack Speed", 0.8, AttributeModifier.Operation.ADD_NUMBER));
    }

    private class SteelScimitarAttackListener implements Listener {
        @EventHandler
        public void onAttack(EntityDamageByEntityEvent event) {
            if (!(event.getEntity() instanceof Damageable damageable))
                return;

            if (!(event.getDamager() instanceof Player player))
                return;

            UUID playerUUID = player.getUniqueId();

            if (event.equals(ignoredEvents.get(playerUUID)))
                return;

            SteelScimitarItemType scimitar = CustomItemType.asInstanceOf(SteelScimitarItemType.class, player.getInventory().getItemInMainHand(), keyManager, resourceManager);

            if (scimitar == null)
                return;

            final double damage = event.getDamage();

            BukkitRunnable runnable = new BukkitRunnable() {
                int runs = 0;
                @Override
                public void run() {
                    if (runs > 1) {
                        cancel();
                        return;
                    }

                    damageable.damage(damage);

                    assert damageable.getLastDamageCause() instanceof EntityDamageByEntityEvent;
                    ignoredEvents.put(playerUUID, (EntityDamageByEntityEvent) damageable.getLastDamageCause());

                    runs++;
                }
            };

            runnable.runTaskTimer(plugin, 10, 10);

        }
    }
}
