package co.tantleffbeef.pluggytesty.villagers;

import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;


public class VillagerTrades implements Listener {

    @EventHandler
    public void onVillagerInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Villager villager)) return;

        switch (villager.getProfession()) {
            case ARMORER -> villager.setRecipes(armorerTrades(0/*playerLevel*/));
        }

    }

    public List<MerchantRecipe> armorerTrades(int level) {
        List<MerchantRecipe> trades = new ArrayList<>();
        trades.add(new MerchantRecipe(new ItemStack(Material.LEATHER_HELMET, 1), 0, 10, false, 0, 1.0f));
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
