package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RapidRapierItemType extends SimpleItemType implements InteractableItemType {

    private final int COOLDOWN_TICKS = 60;

    private final KeyManager<CustomNbtKey> keyManager;
    private final ResourceManager resourceManager;

    public RapidRapierItemType(Plugin namespace, String id, boolean customModel, String name, KeyManager<CustomNbtKey> keyManager, ResourceManager resourceManager) {
        super(namespace, id, customModel, name, Material.DIAMOND_SWORD);

        this.keyManager = keyManager;
        this.resourceManager = resourceManager;

        namespace.getServer().getPluginManager().registerEvents(new RapidRapierAttackListener(), namespace);
    }

    private class RapidRapierAttackListener implements Listener {
        @EventHandler
        public void onAttack(EntityDamageByEntityEvent event) {
            if (!(event.getEntity() instanceof LivingEntity))
                return;

            if (!(event.getDamager() instanceof Player player))
                return;

            RapidRapierItemType rapier = CustomItemType.asInstanceOf(RapidRapierItemType.class, player.getInventory().getItemInMainHand(), keyManager, resourceManager);

            if (rapier == null)
                return;

            event.setDamage(event.getDamage() * player.getVelocity().length() + 0.5);

        }
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack item, @Nullable Block targetBlock) {

        if (player.hasCooldown(Material.DIAMOND_SWORD))
            return false;

        Vector direction = player.getEyeLocation().getDirection();

        player.setVelocity(direction.normalize().multiply(2).add(player.getVelocity()));

        player.setCooldown(Material.DIAMOND_SWORD, COOLDOWN_TICKS);

        return false;
    }
}
