package co.tantleffbeef.pluggytesty.extra_listeners;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import co.tantleffbeef.pluggytesty.custom.item.weapons.FisherOfSoulsItemType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FisherOfSoulsEventListener implements Listener {

    private final KeyManager<CustomNbtKey> keyManager;
    private final ResourceManager resourceManager;
    // holds which players are holding what entity on their hook
    public static final Map<UUID, Entity> hookedEntities = new HashMap<>();

    public FisherOfSoulsEventListener(KeyManager<CustomNbtKey> keyManager, ResourceManager resourceManager) {
        this.keyManager = keyManager;
        this.resourceManager = resourceManager;
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        Bukkit.broadcastMessage("state: " + event.getState());
        if (event.getState() != PlayerFishEvent.State.FISHING)
            return;

        Entity entity = event.getCaught();
        if (entity == null) {
            Bukkit.broadcastMessage("no entity");
            return;
        }

        Bukkit.broadcastMessage("entity: " + entity);

        hookedEntities.put(event.getPlayer().getUniqueId(), entity);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR/* && event.getAction() != Action.LEFT_CLICK_BLOCK*/)
            return;

        Bukkit.broadcastMessage("left click air");
        ItemStack item = event.getItem();
        if (item == null || CustomItemType.asInstanceOf(FisherOfSoulsItemType.class, item, keyManager, resourceManager) == null)
            return;

        Entity entity = hookedEntities.get(event.getPlayer().getUniqueId());
        Bukkit.broadcastMessage("entity: " + entity);

        if (entity == null || entity.isDead())
            return;

        Bukkit.broadcastMessage("exists");

        if (entity instanceof Damageable damageable)
            damageable.damage(5);

    }
}
