package co.tantleffbeef.pluggytesty.custom.item.armor;
import co.tantleffbeef.pluggytesty.armor.BaseArmor;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static org.bukkit.Bukkit.getRecipe;


public class CraftListener implements Listener {

    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
        final CraftingInventory inventory = event.getInventory();
        final ItemStack result = inventory.getResult();

        if (result == null)
            return;


        if (result.equals(getRecipe(NamespacedKey.minecraft("leather_helmet")).getResult())) {
            event.getInventory().setResult(BaseArmor.lH());
        }
        if (result.equals(getRecipe(NamespacedKey.minecraft("leather_chestplate")).getResult())) {
            event.getInventory().setResult(BaseArmor.lC());
        }
        if (result.equals(getRecipe(NamespacedKey.minecraft("leather_leggings")).getResult())) {
            event.getInventory().setResult(BaseArmor.lL());
        }
        if (result.equals(getRecipe(NamespacedKey.minecraft("leather_boots")).getResult())) {
            event.getInventory().setResult(BaseArmor.lB());
        }
        if (result.equals(getRecipe(NamespacedKey.minecraft("iron_helmet")).getResult())) {
            event.getInventory().setResult(BaseArmor.iH());
        }
        if (result.equals(getRecipe(NamespacedKey.minecraft("iron_chestplate")).getResult())) {
            event.getInventory().setResult(BaseArmor.iC());
        }
        if (result.equals(getRecipe(NamespacedKey.minecraft("iron_leggings")).getResult())) {
            event.getInventory().setResult(BaseArmor.iL());
        }
        if (result.equals(getRecipe(NamespacedKey.minecraft("iron_boots")).getResult())) {
            event.getInventory().setResult(BaseArmor.iB());
        }
        if (result.equals(getRecipe(NamespacedKey.minecraft("golden_helmet")).getResult())) {
            event.getInventory().setResult(BaseArmor.gH());
        }
        if (result.equals(getRecipe(NamespacedKey.minecraft("golden_chestplate")).getResult())) {
            event.getInventory().setResult(BaseArmor.gC());
        }
        if (result.equals(getRecipe(NamespacedKey.minecraft("golden_leggings")).getResult())) {
            event.getInventory().setResult(BaseArmor.gL());
        }
        if (result.equals(getRecipe(NamespacedKey.minecraft("golden_boots")).getResult())) {
            event.getInventory().setResult(BaseArmor.gB());
        }
        if (result.equals(getRecipe(NamespacedKey.minecraft("diamond_helmet")).getResult())) {
            event.getInventory().setResult(BaseArmor.dH());
        }
        if (result.equals(getRecipe(NamespacedKey.minecraft("diamond_chestplate")).getResult())) {
            event.getInventory().setResult(BaseArmor.dC());
        }
        if (result.equals(getRecipe(NamespacedKey.minecraft("diamond_leggings")).getResult())) {
            event.getInventory().setResult(BaseArmor.dL());
        }
        if (result.equals(getRecipe(NamespacedKey.minecraft("diamond_boots")).getResult())) {
            event.getInventory().setResult(BaseArmor.dB());
        }
    }
}

