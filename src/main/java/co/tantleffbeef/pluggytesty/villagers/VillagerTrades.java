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

    private List<MerchantRecipe> createRecipe(int input, List<ItemStack> output, int num) {
        Random r =  new Random();
        List<ItemStack> temp = new ArrayList<>();
        temp.addAll(output);
        List<MerchantRecipe> resList = new ArrayList<>();

        for(int i = 0; i < num; i++) {
            MerchantRecipe res = new MerchantRecipe(temp.remove(r.nextInt(output.size())), 0, 10, false, 0, 0f);
            res.addIngredient(new ItemStack(Material.EMERALD, input));
            resList.add(res);
        }

        return resList;
    }

    private List<MerchantRecipe> createRecipe(ItemStack input, int input2, List<ItemStack> output, int num) {
        Random r =  new Random();
        ArrayList<ItemStack> temp = new ArrayList<>();
        temp.addAll(output);
        List<MerchantRecipe> resList = new ArrayList<>();

        for(int i = 0; i < num; i++) {
            MerchantRecipe res = new MerchantRecipe(temp.remove(r.nextInt(output.size())), 0, 10, false, 0, 0f);
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

        int profInt = 0;
        switch (prof) {
            case ARMORER -> profInt = 0;
            case BUTCHER -> profInt = 1;
            case CARTOGRAPHER -> profInt = 2;
            case CLERIC -> profInt = 3;
            case FARMER -> profInt = 4;
            case FISHERMAN -> profInt = 5;
            case LEATHERWORKER -> profInt = 6;
            case LIBRARIAN -> profInt = 7;
            case MASON -> profInt = 8;
            case SHEPHERD -> profInt = 9;
            case TOOLSMITH -> profInt = 10;
            case WEAPONSMITH -> profInt = 11;
        }

        int numOfTrades = 0;
        for(int i = 0; i < exp; i++) {
            numOfTrades += tradeAmts[profInt][i];
        }

        if(numOfTrades != trades.size() - 1) {
            trades.remove(trades.size() - 1);

            // add new trades
            // add new final trade
        }


        if(pLevel < exp - 1) { // pLevel goes from 0-5 and exp goes from 1-5
            int l = 0;
            for(int h = pLevel; h < exp - 1; h++) {
                l += tradeAmts[profInt][h];
            }

            for(int i = l; i < trades.size(); i++) {
                trades.get(i).setMaxUses(0); // see if having a maxUses below the Uses gives an error
            }
        } else {
            for(int i = 0; i < trades.size(); i++) {
                trades.get(i).setMaxUses(10);
            }
        }

        return trades;
    }


}
