package co.tantleffbeef.pluggytesty.villagers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class VillagerTrades implements Listener {

    @EventHandler
    public void onVillagerInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Villager villager)) return;

        switch (villager.getProfession()) {
            case ARMORER -> villager.setRecipes(armorerTrades(new Random().nextInt(6)));
        }

    }

    private MerchantRecipe createRecipe(ItemStack input, ItemStack output) {
        MerchantRecipe res = new MerchantRecipe(output, 0, 10, false, 0, 1.0f);
        res.addIngredient(input);
        return res;
    }

    public List<MerchantRecipe> armorerTrades(int level) {
        List<MerchantRecipe> trades = new ArrayList<>();

        trades.add(createRecipe(new ItemStack(Material.LEATHER, 5), new ItemStack(Material.LEATHER_HELMET, 1)));
        if(level >= 1) {
            trades.add(createRecipe(new ItemStack(Material.CHAINMAIL_HELMET, 1), new ItemStack(Material.CHAIN, 5)));
        }
        if(level >= 2) {
            trades.add(createRecipe(new ItemStack(Material.IRON_HELMET, 1), new ItemStack(Material.IRON_INGOT, 5)));
        }
        if(level >= 3) {
            trades.add(createRecipe(new ItemStack(Material.GOLDEN_HELMET, 1), new ItemStack(Material.GOLD_INGOT, 5)));
        }
        if(level >= 4) {
            trades.add(createRecipe(new ItemStack(Material.DIAMOND_HELMET, 1), new ItemStack(Material.DIAMOND, 5)));
        }
        if(level >= 5) {
            trades.add(createRecipe(new ItemStack(Material.NETHERITE_HELMET, 1), new ItemStack(Material.NETHERITE_INGOT, 5)));
        }

        return trades;
    }


}
// Armorer should be updated to:
// Novice: 5 Leather for Leather Helmet (gives 0 XP to villager)
// Novice: 5 Chains for Chain Helmet (gives 0 XP to villager)
// Novice: 1 Emerald for 1 Brown Due (gives 10 XP to villager, letting it level up)
// Apprentice: 5 Iron for Iron Helmet (gives 0 XP to villager)
// Apprentice: 1 Emerald for 1 Yellow Due (gives 70 XP to villager, letting it level up)
// Journeyman: 5 Gold for Golden Helmet (gives 0 XP to villager)
// Journeyman: 1 Emerald for 1 Green Due (gives 150 XP to villager, letting it level up)
// Expert: 5 Diamond for Diamond Helmet (gives 0 XP to villager)
// Expert: 1 Emerald for 1 Blue Due (gives 250 XP to villager, letting it level up)
// Master: 5 Netherite for Netherite Helmet (gives 0 XP to villager)

// Each trade that gives XP should be removed once the villager levels up.
