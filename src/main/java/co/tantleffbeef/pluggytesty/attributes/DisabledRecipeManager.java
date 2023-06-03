package co.tantleffbeef.pluggytesty.attributes;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import co.tantleffbeef.mcplanes.pojo.serialize.CustomItemNbt;
import co.tantleffbeef.pluggytesty.goober.Goober;
import co.tantleffbeef.pluggytesty.goober.GooberStateController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import static java.util.Map.entry;

public class DisabledRecipeManager implements Listener {

    private final Plugin plugin;
    private final GooberStateController gooberStateController;
    private final KeyManager<CustomNbtKey> keyManager;

    // how do I know if I use attribute or resource manager?????
    private final AttributeManager attributeManager;

    // map containing NamespacedKeys of banned RECIPES and player level requirements
    private final Map<NamespacedKey, Integer> disabledRecipes;

    // map containing NamespacedKeys of banned ITEMS and player level requirements
    private final Map<NamespacedKey, Integer> disabledItems;

    private final int DISABLED = 10;

    public DisabledRecipeManager(Plugin plugin, AttributeManager attributeManager, GooberStateController gooberStateController, KeyManager<CustomNbtKey> keyManager) {
        this.plugin = plugin;
        this.attributeManager = attributeManager;
        this.gooberStateController = gooberStateController;
        this.keyManager = keyManager;

        disabledRecipes = Map.ofEntries(
                entry(new NamespacedKey(plugin, "bolt_rod"), 3),
                entry(NamespacedKey.minecraft("chain"), DISABLED),
                entry(NamespacedKey.minecraft("netherite_leggings"), 2)

        );

        disabledItems = Map.ofEntries(
                entry(NamespacedKey.minecraft("bread"), 2),
                entry(NamespacedKey.minecraft("leather"), 1),
                entry(NamespacedKey.minecraft("carrot"), 0),
                entry(NamespacedKey.minecraft("potato"), -1),
                entry(new NamespacedKey(plugin, "magic_stick"), 4)
        );

    }

    private void checkItem(Goober player, NamespacedKey key, Cancellable event) {
        // level at which picked up item is unlocked
        Integer requiredLevel = disabledItems.get(key);

        if (requiredLevel == null)
            return;

        // if the player does not have high enough level, don't let them do that dawg
        if (player.getLevel() < requiredLevel)
            event.setCancelled(true);
    }

    private void checkRecipe(Goober player, NamespacedKey key, Cancellable event) {
        Integer requiredLevel = disabledRecipes.get(key);

        if (requiredLevel == null)
            return;

        if (player.getLevel() < requiredLevel) {
            event.setCancelled(true);
            player.asPlayer().sendMessage(ChatColor.RED + "You are not high enough level to do that!");
        }
    }

    @EventHandler
    public void onPlayerPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player tempPlayer))
            return;

        Goober player = gooberStateController.wrapPlayer(tempPlayer);

        checkItem(player, CustomItemNbt.customItemIdOrVanilla(event.getItem().getItemStack(), keyManager), event);
    }

    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null)
            return;

        if (!(event.getWhoClicked() instanceof Player tempPlayer))
            return;

        Goober player = gooberStateController.wrapPlayer(tempPlayer);

        // check if the item is banned
        checkItem(player, CustomItemNbt.customItemIdOrVanilla(item, keyManager), event);

        // check the if the recipe is banned if it is a crafting or smith event
        if (event instanceof CraftItemEvent craftEvent) {
            Recipe recipe = craftEvent.getRecipe();

            if (!(recipe instanceof Keyed keyedRecipe) || event.isCancelled())
                return;

            checkRecipe(player, keyedRecipe.getKey(), event);

        } else if (event instanceof SmithItemEvent smithEvent) {
            Recipe recipe = smithEvent.getInventory().getRecipe();

            if (!(recipe instanceof Keyed keyedRecipe) || event.isCancelled())
                return;

            checkRecipe(player, keyedRecipe.getKey(), event);
        }
    }

}