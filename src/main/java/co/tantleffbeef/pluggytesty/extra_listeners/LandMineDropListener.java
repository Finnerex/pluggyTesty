package co.tantleffbeef.pluggytesty.extra_listeners;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import co.tantleffbeef.pluggytesty.custom.item.utility.LandMineItemType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public class LandMineDropListener implements Listener {

    private final KeyManager<CustomNbtKey> keyManager;
    private final ResourceManager resourceManager;
    private final Plugin plugin;

    public LandMineDropListener(KeyManager<CustomNbtKey> keyManager, ResourceManager resourceManager, Plugin plugin) {
        this.keyManager = keyManager;
        this.resourceManager = resourceManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        if (CustomItemType.asInstanceOf(LandMineItemType.class, item.getItemStack(), keyManager, resourceManager) == null)
            return;

        World world = item.getWorld();

        // this could get very resource intensive with a lot of them but idk
        BukkitRunnable runnable = new BukkitRunnable() {
            ItemDisplay itemDisplay = null;
            @Override
            public void run() {
                if (item.isOnGround() && !item.isDead()) {
                    itemDisplay = world.spawn(item.getLocation(), ItemDisplay.class, (display) -> {
                        display.setRotation(0, 0);
                        display.setItemStack(new ItemStack(Material.FIREWORK_STAR));
                    });

                    item.remove();
                } else if (!item.isOnGround() && !item.isDead())
                    return;

                Collection<Entity> entities = world.getNearbyEntities(itemDisplay.getLocation(), 1, 1, 1);
                entities.remove(itemDisplay);
                entities.remove(item);

                if (!entities.isEmpty()) {
                    world.createExplosion(itemDisplay.getLocation(), 2, false, false);
                    itemDisplay.remove();
                    cancel();
                }

            }
        };

        runnable.runTaskTimer(plugin, 40, 10);

    }
}
