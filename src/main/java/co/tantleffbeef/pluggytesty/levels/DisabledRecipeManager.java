package co.tantleffbeef.pluggytesty.levels;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.pojo.serialize.CustomItemNbt;
import co.tantleffbeef.pluggytesty.attributes.AttributeManager;
import co.tantleffbeef.pluggytesty.goober.Goober;
import co.tantleffbeef.pluggytesty.goober.GooberStateController;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import static java.util.Map.entry;

public class DisabledRecipeManager implements Listener {

    private final GooberStateController gooberStateController;
    private final KeyManager<CustomNbtKey> keyManager;


    // map containing NamespacedKeys of banned RECIPES and player level requirements
    // only use this when a recipe is disabled but not the item (such as chains)
    private final Map<NamespacedKey, Integer> disabledRecipes;

    // map containing NamespacedKeys of banned ITEMS and player level requirements
    private final Map<NamespacedKey, Integer> disabledItems;

    private final int DISABLED = 10;

    public DisabledRecipeManager(Plugin plugin, GooberStateController gooberStateController, KeyManager<CustomNbtKey> keyManager) {
        this.gooberStateController = gooberStateController;
        this.keyManager = keyManager;

        disabledRecipes = Map.ofEntries(
                // custom wie das: entry(new NamespacedKey(plugin, "bolt_rod"), 3),
                entry(NamespacedKey.minecraft("chain"), DISABLED)
        );

        disabledItems = Map.ofEntries(
                // level 1
                entry(NamespacedKey.minecraft("chainmail_boots"), 1),
                entry(NamespacedKey.minecraft("chainmail_leggings"), 1),
                entry(NamespacedKey.minecraft("chainmail_chestplate"), 1),
                entry(NamespacedKey.minecraft("chainmail_helmet"), 1),
                entry(NamespacedKey.minecraft("stone_pickaxe"), 1),
                entry(NamespacedKey.minecraft("stone_axe"), 1),
                entry(NamespacedKey.minecraft("stone_hoe"), 1),
                entry(NamespacedKey.minecraft("stone_shovel"), 1),
                entry(NamespacedKey.minecraft("stone_sword"), 1),

                // level 2
                entry(NamespacedKey.minecraft("iron_boots"), 2),
                entry(NamespacedKey.minecraft("iron_leggings"), 2),
                entry(NamespacedKey.minecraft("iron_chestplate"), 2),
                entry(NamespacedKey.minecraft("iron_helmet"), 2),
                entry(NamespacedKey.minecraft("iron_pickaxe"), 2),
                entry(NamespacedKey.minecraft("iron_axe"), 2),
                entry(NamespacedKey.minecraft("iron_hoe"), 2),
                entry(NamespacedKey.minecraft("iron_shovel"), 2),
                entry(NamespacedKey.minecraft("iron_sword"), 2),

                // level 3
                entry(NamespacedKey.minecraft("gold_boots"), 3),
                entry(NamespacedKey.minecraft("gold_leggings"), 3),
                entry(NamespacedKey.minecraft("gold_chestplate"), 3),
                entry(NamespacedKey.minecraft("gold_helmet"), 3),
                entry(NamespacedKey.minecraft("gold_pickaxe"), 3),
                entry(NamespacedKey.minecraft("gold_axe"), 3),
                entry(NamespacedKey.minecraft("gold_hoe"), 3),
                entry(NamespacedKey.minecraft("gold_shovel"), 3),
                entry(NamespacedKey.minecraft("gold_sword"), 3),

                // level 4
                entry(NamespacedKey.minecraft("diamond_boots"), 4),
                entry(NamespacedKey.minecraft("diamond_leggings"), 4),
                entry(NamespacedKey.minecraft("diamond_chestplate"), 4),
                entry(NamespacedKey.minecraft("diamond_helmet"), 4),
                entry(NamespacedKey.minecraft("diamond_pickaxe"), 4),
                entry(NamespacedKey.minecraft("diamond_axe"), 4),
                entry(NamespacedKey.minecraft("diamond_hoe"), 4),
                entry(NamespacedKey.minecraft("diamond_shovel"), 4),
                entry(NamespacedKey.minecraft("diamond_sword"), 4),

                // level 5
                entry(NamespacedKey.minecraft("netherite_boots"), 5),
                entry(NamespacedKey.minecraft("netherite_leggings"), 5),
                entry(NamespacedKey.minecraft("netherite_chestplate"), 5),
                entry(NamespacedKey.minecraft("netherite_helmet"), 5),
                entry(NamespacedKey.minecraft("netherite_pickaxe"), 5),
                entry(NamespacedKey.minecraft("netherite_axe"), 5),
                entry(NamespacedKey.minecraft("netherite_hoe"), 5),
                entry(NamespacedKey.minecraft("netherite_shovel"), 5),
                entry(NamespacedKey.minecraft("netherite_sword"), 5),

                // Disabled
                entry(NamespacedKey.minecraft("enchanting_table"), DISABLED)
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

            if (requiredLevel == DISABLED)
                player.asPlayer().sendMessage(ChatColor.RED + "This recipe is disabled!");
            else
                player.asPlayer().sendMessage(ChatColor.RED + "You are not high enough level to do that!");
        }
    }

    @EventHandler
    public void onPlayerPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player tempPlayer))
            return;

        if (tempPlayer.getGameMode() == GameMode.CREATIVE)
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

        if (tempPlayer.getGameMode() == GameMode.CREATIVE)
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

        }
    }

}