package co.tantleffbeef.pluggytesty.villagers;

import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.*;


public class VillagerTradesListener implements Listener {

    @EventHandler
    public void onVillagerInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Villager vil)) return;

        List<MerchantRecipe> trades = new ArrayList<>();
        trades = vil.getRecipes();
        int exp = vil.getVillagerLevel();
        Villager.Profession prof = vil.getProfession();
        for(int i = 0; i < trades.size(); i++) {
            if(isVanillaTrade(trades.get(i))) {
                trades.remove(i);
            }
        }

//        if(prof == Villager.Profession.UNEMPLOYED || prof == Villager.Profession.NITWIT) return;
//
//        int numExpectedTrades = 0;
//        for(int i = 0; i < exp; i++) {
//            numExpectedTrades += TradeSilo.tradeAmts[tradeAmts.get(prof)][i];
//        }
//
//        if(numExpectedTrades != trades.size() - 1) { // if the number of expected trades doesn't match up with the number of trades (excluding upgrade one)...
//            if(trades.size() > 0) trades.remove(trades.size() - 1); // we assume that the villager levelled up and so remove the ending trade.
//            List<MerchantRecipe> options;
//            switch(prof) {
//                    case ARMORER -> options = armorerTrades.get(exp).clone();
//                    case BUTCHER -> options = butcherTrades.get(exp).clone();
//                    case CLERIC -> options = armorerTrades.get(exp).clone();
//                    case FARMER -> options = butcherTrades.get(exp).clone();
//                    case FISHERMAN -> options = armorerTrades.get(exp).clone();
//                    case LIBRARIAN -> options = butcherTrades.get(exp).clone();
//                    case MASON -> options = armorerTrades.get(exp).clone();
//                    case TOOLSMITH -> options = butcherTrades.get(exp).clone();
//                    case WEAPONSMITH -> options = armorerTrades.get(exp).clone();
//            }
            
            trades.add(TradeSilo.upgradeRecipe(exp));
            // add new trades
        }
//    }
// At this point, I have the villager's profession, trades, and exp
// Next, I should remove all vanilla trades (those that involve emeralds) - DONE
// Then, check if trades.size() - 1 is the expected value in tradeAmts to see if it has the correct number of trades for its villager level. - DONE
// If the villager doesn't have the right number of trades, remove its last trade and add its new ones, adding a new last trade on.
// Seperately, check if the player is lower levelled than the villager.
// If so, cause all of that villager's trades to have a maxUses of their current uses.
// If not, cause the villager's trades to have a maxUses of 10.



    private boolean isVanillaTrade(MerchantRecipe trade) {
        for(ItemStack item : trade.getIngredients()) {
            if(item.getType() == Material.EMERALD) return true;
        }
        if(trade.getResult().getType() == Material.EMERALD) return true;

        return false;
    }

}

// int numOfTrades = 0;
//        for(int i = 0; i < exp; i++) {
//            numOfTrades += tradeAmts[profInt][i];
//        }
//
//        if(numOfTrades != trades.size() - 1) {
//            trades.remove(trades.size() - 1);
//
//            // add new trades
//            // add new final trade
//        }
//
//
//        if(pLevel < exp - 1) { // pLevel goes from 0-5 and exp goes from 1-5
//            int l = 0;
//            for(int h = pLevel; h < exp - 1; h++) {
//                l += tradeAmts[profInt][h];
//            }
//
//            for(int i = l; i < trades.size(); i++) {
//                trades.get(i).setMaxUses(0); // see if having a maxUses below the Uses gives an error
//            }
//        } else {
//            for(int i = 0; i < trades.size(); i++) {
//                trades.get(i).setMaxUses(10);
//            }
//        }
//
//        return trades;
