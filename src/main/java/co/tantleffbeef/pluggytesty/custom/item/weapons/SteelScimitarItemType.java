package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
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

    private final Map<UUID, Boolean> attacking;

    private final Plugin plugin;

    public SteelScimitarItemType(Plugin namespace, String id, boolean customModel, String name, KeyManager<CustomNbtKey> keyManager, ResourceManager resourceManager) {
        super(namespace, id, customModel, name, Material.STONE_SWORD);
        this.keyManager = keyManager;
        this.resourceManager = resourceManager;
        attacking = new HashMap<>();
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

            if (!(event.getEntity() instanceof LivingEntity livingEntity))
                return;

            if (!(event.getDamager() instanceof Player player))
                return;

            Bukkit.broadcastMessage("dmgbl dmg by player");

            UUID playerUUID = player.getUniqueId();
            attacking.putIfAbsent(playerUUID, false);

            if (attacking.get(playerUUID))
                return;

            Bukkit.broadcastMessage("player is not attacking");

            SteelScimitarItemType scimitar = CustomItemType.asInstanceOf(SteelScimitarItemType.class, player.getInventory().getItemInMainHand(), keyManager, resourceManager);

            if (scimitar == null)
                return;

            Bukkit.broadcastMessage("player holding scimitar");

            final double damage = event.getDamage();
            attacking.put(playerUUID, true);

            BukkitRunnable runnable = new BukkitRunnable() {
                int runs = 0;
                @Override
                public void run() {
                    if (runs > 1) {
                        cancel();
                        attacking.put(playerUUID, false);
                        return;
                    }

                    livingEntity.setNoDamageTicks(0);

                    livingEntity.damage(damage, player);

                    runs++;
                }
            };

            runnable.runTaskTimer(plugin, 5, 5);

        }
    }
}
