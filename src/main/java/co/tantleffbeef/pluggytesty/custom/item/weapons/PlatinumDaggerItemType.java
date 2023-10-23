package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

public class PlatinumDaggerItemType extends SimpleItemType {

    private final KeyManager<CustomNbtKey> keyManager;
    private final ResourceManager resourceManager;

    public PlatinumDaggerItemType(Plugin namespace, String id, boolean customModel, String name, KeyManager<CustomNbtKey> keyManager, ResourceManager resourceManager) {
        super(namespace, id, customModel, name);
        this.keyManager = keyManager;
        this.resourceManager = resourceManager;

        namespace.getServer().getPluginManager().registerEvents(new PlatinumDaggerAttackListener(), namespace);
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
