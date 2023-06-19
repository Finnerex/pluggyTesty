package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FisherOfSoulsItemType extends SimpleItemType implements Listener {

    private final KeyManager<CustomNbtKey> keyManager;
    private final ResourceManager resourceManager;

    // holds which players are holding what entity on their hook
    private final Map<UUID, Entity> hookedEntities = new HashMap<>();

    public FisherOfSoulsItemType(Plugin namespace, String id, boolean customModel, String name, KeyManager<CustomNbtKey> keyManager, ResourceManager resourceManager) {
        super(namespace, id, customModel, name, Material.FISHING_ROD);
        this.resourceManager = resourceManager;
        this.keyManager = keyManager;
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);
        meta.setLore(List.of(ChatColor.DARK_GREEN + "Left-Click : Damage hooked enemy"));
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_ENTITY)
            return;

        Entity entity = event.getCaught();
        assert entity != null;

        hookedEntities.put(event.getPlayer().getUniqueId(), entity);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK)
            return;

        ItemStack item = event.getItem();
        if (item == null || CustomItemType.asInstanceOf(FisherOfSoulsItemType.class, item, keyManager, resourceManager) == null)
            return;

        Entity entity = hookedEntities.get(event.getPlayer().getUniqueId());

        if (entity == null || entity.isDead())
            return;

        if (entity instanceof Damageable damageable)
            damageable.damage(3);

    }

}
