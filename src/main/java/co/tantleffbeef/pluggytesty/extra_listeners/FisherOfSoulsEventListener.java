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

public class FisherOfSoulsEventListener implements Listener {

    private final KeyManager<CustomNbtKey> keyManager;
    private final ResourceManager resourceManager;

    public FisherOfSoulsEventListener(KeyManager<CustomNbtKey> keyManager, ResourceManager resourceManager) {
        this.keyManager = keyManager;
        this.resourceManager = resourceManager;
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_ENTITY)
            return;

        Entity entity = event.getCaught();
        assert entity != null;

        Bukkit.broadcastMessage("entity: " + entity);

        FisherOfSoulsItemType.hookedEntities.put(event.getPlayer().getUniqueId(), entity);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Bukkit.broadcastMessage("evemt");
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK)
            return;

        Bukkit.broadcastMessage("left");
        ItemStack item = event.getItem();
        if (item == null || CustomItemType.asInstanceOf(FisherOfSoulsItemType.class, item, keyManager, resourceManager) == null)
            return;
        Bukkit.broadcastMessage("fisher");

        Entity entity = FisherOfSoulsItemType.hookedEntities.get(event.getPlayer().getUniqueId());
        Bukkit.broadcastMessage("entity: " + entity);

        if (entity == null || entity.isDead())
            return;
        Bukkit.broadcastMessage("exists");

        if (entity instanceof Damageable damageable) {
            damageable.damage(3);
            Bukkit.broadcastMessage("damgegad");
        }

    }
}
