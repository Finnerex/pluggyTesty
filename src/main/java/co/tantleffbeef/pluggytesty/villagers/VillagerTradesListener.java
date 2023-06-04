package co.tantleffbeef.pluggytesty.villagers;

import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import co.tantleffbeef.pluggytesty.goober.GooberStateController;
import co.tantleffbeef.pluggytesty.goober.Goober;

import java.util.*;


public class VillagerTradesListener implements Listener {
    private final GooberStateController iHateThis;
    public VillagerTradesListener(GooberStateController thisIsDumb) {
        iHateThis = thisIsDumb;
    }

    private boolean isVanillaTrade(MerchantRecipe trade) { // returns true if any part of the trade involves emeralds, otherwise false
        for(ItemStack item : trade.getIngredients()) {
            if(item.getType() == Material.EMERALD) return true;
        }
        if(trade.getResult().getType() == Material.EMERALD) return true;
        return false;
    }
    @EventHandler
    public void onVillagerInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Villager vil)) return;

        List<MerchantRecipe> trades = new ArrayList<>(vil.getRecipes());
        int exp = vil.getVillagerLevel();
        Goober player = iHateThis.wrapPlayer(event.getPlayer());
        Villager.Profession prof = vil.getProfession();

        for(int i = trades.size() - 1; i >= 0; i--) {
            if(isVanillaTrade(trades.get(i))) {
                trades.remove(i);

            }
        }

        if(prof == Villager.Profession.NONE || prof == Villager.Profession.NITWIT) return; // this would mess things up later so we return now

        int numExpectedTrades = 1; // starts at 1 due to ending trade

        for(int i = 0; i < exp; i++) {
            numExpectedTrades += TradeSilo.tradeAmts.get(prof)[i]; // calculate the number of trades we expect villager to have
        }


        if(numExpectedTrades != trades.size()) { // if the number of expected trades doesn't match up with the number of trades (excluding upgrade one)...

            for(int i = 0; i < trades.size(); i++) {
                if (trades.get(i).getVillagerExperience() > 0) {
                    trades.remove(i); // we assume that the villager levelled up and so remove the ending trade.
                }
            }

            List<MerchantRecipe> options = new ArrayList<>(switch (prof) { // establishing the list of trades we can choose from
                case ARMORER -> TradeSilo.armorerTrades.get(exp);
                case BUTCHER -> TradeSilo.butcherTrades.get(exp);
                case CLERIC -> TradeSilo.clericTrades.get(exp);
                case FARMER -> TradeSilo.farmerTrades.get(exp);
                case FISHERMAN -> TradeSilo.fishermanTrades.get(exp);
                case LIBRARIAN -> TradeSilo.librarianTrades.get(exp);
                case MASON -> TradeSilo.masonTrades.get(exp);
                case TOOLSMITH -> TradeSilo.toolsmithTrades.get(exp);
                case WEAPONSMITH -> TradeSilo.weaponsmithTrades.get(exp);
                default -> TradeSilo.librarianTrades.get(exp);
            });

            for(int i = 0; i < TradeSilo.tradeAmts.get(prof)[exp-1]; i++) {
                trades.add(options.remove(new Random().nextInt(options.size()))); // Add new trades according to tradeAmts, use .remove() to prevent duplicates
            }

            trades.add(TradeSilo.upgradeRecipe(exp)); // adding new final trade
        }
        event.getPlayer().sendMessage("Level: " + player.getLevel());
        int pLevel = player.getLevel();

        if(pLevel < exp) { // pLevel goes from 0-5 and exp goes from 1-5
            int l = 0;
            for(int h = pLevel; h < exp; h++) {
                l += TradeSilo.tradeAmts.get(prof)[h]; // this determines how many trades the player should have access to so they don't become unavailable
            }

            for(int i = l; i < trades.size(); i++) {
                trades.get(i).setMaxUses(trades.get(i).getUses()); // setting a trade's max uses to be its current uses will disable it
            }
        } else {
            for(int i = 0; i < trades.size(); i++) { // set the trade's max uses to 10, fixing trades if they were previously opened by some low level person
                trades.get(i).setMaxUses(10);
            }
        }

        vil.setRecipes(trades);
    }



}

