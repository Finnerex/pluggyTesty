package co.tantleffbeef.pluggytesty.attributes;

import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import co.tantleffbeef.pluggytesty.goober.Goober;
import co.tantleffbeef.pluggytesty.goober.GooberStateController;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import static java.util.Map.entry;

public class DisabledRecipeManager implements Listener {

    private final Plugin plugin;
    private final GooberStateController gooberStateController;

    // how do I know if I use attribute or resource manager?????
    private final AttributeManager attributeManager;

    // map containing NamespacedKeys of banned RECIPES and player level requirements
    private final Map<NamespacedKey, Integer> disabledRecipes;

    // map containing NamespacedKeys of banned ITEMS and player level requirements
    private final Map<NamespacedKey, Integer> disabledItems;

    private final int DISABLED = 10;

    public DisabledRecipeManager(Plugin plugin, AttributeManager attributeManager, GooberStateController gooberStateController) {
        this.plugin = plugin;
        this.attributeManager = attributeManager;
        this.gooberStateController = gooberStateController;

        disabledRecipes = Map.ofEntries(
                entry(new NamespacedKey(plugin, "bolt_rod"), DISABLED)

        );

        disabledItems = Map.ofEntries(
                entry(NamespacedKey.minecraft("bread"), 2),
                entry(NamespacedKey.minecraft("leather"), 1),
                entry(NamespacedKey.minecraft("carrot"), 0),
                entry(NamespacedKey.minecraft("potato"), -1)
        );

    }

    @EventHandler
    public void onPlayerPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player tempPlayer))
            return;

        Goober player = gooberStateController.wrapPlayer(tempPlayer);

        // level at which picked up item is unlocked
        Integer requiredLevel = disabledItems.get(event.getItem().getItemStack().getType().getKey());
        Bukkit.broadcastMessage("Player level: " + player.getLevel() + "\nRequired level: " + requiredLevel);

        if (requiredLevel == null)
            return;

        if (player.getLevel() < requiredLevel)
            event.setCancelled(true);

    }

    @EventHandler
    public void onPlayerInventoryInteract(InventoryInteractEvent event) {

    }

}
