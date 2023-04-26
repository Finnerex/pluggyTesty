package co.tantleffbeef.pluggytesty.villagers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
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

    public static int tradeAmts[][] = {
            {2, 1, 1, 1, 2}, // Armorer
            {2, 2, 1, 2, 1}, // Butcher
            {0, 0, 0, 0, 0}, // Cartographer
            {3, 2, 1, 1, 3}, // Cleric
            {2, 2, 1, 1, 1}, // Farmer
            {2, 1, 1, 1, 1}, // Fisherman
            {0, 0, 0, 0, 0}, // Fletcher
            {0, 0, 0, 0, 0}, // Leatherworker
            {2, 2, 2, 2, 2}, // Librarian
            {2, 2, 2, 1, 2}, // Mason
            {0, 0, 0, 0, 0}, // Shepherd
            {2, 2, 2, 2, 2}, // Toolsmith
            {1, 1, 1, 1, 1}, // Weaponsmith
    };


    @EventHandler
    public void onVillagerInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Villager villager)) return;
        villager.setRecipes(updateTrades(new Random().nextInt(6), villager));
        Villager.Profession prof = villager.getProfession();
        switch (prof) {
//            case ARMORER -> return;
        }

    }

    private ArrayList<MerchantRecipe> createRecipe(int input, ArrayList<ItemStack> output, int num) {
        Random r =  new Random();
        ArrayList<MerchantRecipe> resList = new ArrayList<>();

        for(int i = 0; i < num; i++) {
            MerchantRecipe res = new MerchantRecipe(output.remove(r.nextInt(output.size())), 0, 10, false, 0, 0f);
            res.addIngredient(new ItemStack(Material.EMERALD, input));
            resList.add(res);
        }

        return resList;
    }

    private List<MerchantRecipe> createRecipe(ItemStack input, int input2, List<ItemStack> output, int num) {
        Random r =  new Random();
        List<MerchantRecipe> resList = new ArrayList<>();

        for(int i = 0; i < num; i++) {
            MerchantRecipe res = new MerchantRecipe(output.remove(r.nextInt(output.size())), 0, 10, false, 0, 0f);
            res.addIngredient(input);
            res.addIngredient(new ItemStack(Material.EMERALD, input2));
            resList.add(res);
        }

        return resList;
    }

    public List<MerchantRecipe> updateTrades(int pLevel, Villager vil) {
        List<MerchantRecipe> trades = new ArrayList<>();
        trades = vil.getRecipes();
        int exp = vil.getVillagerLevel();
        Villager.Profession prof = vil.getProfession();
// check if the villager is the correct level by comparing its level to the number of trades it should have at that level
// if villager level is bad, remove its final trade (the level up one), add its new trades, and then add the new level up trade
// if the player level is lower than the villager level, find all trades that are too high level and make them out of stock.
        return trades;
    }


}
