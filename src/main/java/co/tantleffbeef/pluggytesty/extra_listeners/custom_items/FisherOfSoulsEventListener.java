package co.tantleffbeef.pluggytesty.extra_listeners.custom_items;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import co.tantleffbeef.pluggytesty.custom.item.weapons.FisherOfSoulsItemType;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
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
    // holds which players have what fishing hook
    public static final Map<UUID, FishHook> hooks = new HashMap<>();

    public FisherOfSoulsEventListener(KeyManager<CustomNbtKey> keyManager, ResourceManager resourceManager) {
        this.keyManager = keyManager;
        this.resourceManager = resourceManager;
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.FISHING)
            return;

        hooks.put(event.getPlayer().getUniqueId(), event.getHook());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK)
            return;

        ItemStack item = event.getItem();
        if (item == null || CustomItemType.asInstanceOf(FisherOfSoulsItemType.class, item, keyManager, resourceManager) == null)
            return;

        FishHook hook = hooks.get(event.getPlayer().getUniqueId());
        Entity entity = hook.getHookedEntity();

        if (entity == null || entity.isDead() || hook.isDead())
            return;

        if (entity instanceof Damageable damageable)
            damageable.damage(5, event.getPlayer());

    }
}
