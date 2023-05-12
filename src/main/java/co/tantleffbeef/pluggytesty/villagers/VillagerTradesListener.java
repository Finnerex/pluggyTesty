package co.tantleffbeef.pluggytesty.villagers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.*;


public class VillagerTradesListener implements Listener {
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
        Player player = event.getPlayer();
        Villager.Profession prof = vil.getProfession();

        for(int i = 0; i < trades.size(); i++) {
            MerchantRecipe e = trades.get(i);
            player.sendMessage(ChatColor.RED + "Removed Trade: " + e.getIngredients().get(0).toString() + ", " + e.getResult().toString());
            if(isVanillaTrade(trades.get(i))) {
                trades.remove(i);

            }
        }

        if(prof == Villager.Profession.NONE || prof == Villager.Profession.NITWIT) return; // this would mess things up later so we return now

        int numExpectedTrades = 1; // starts at 1 due to ending trade

        for(int i = 0; i < exp; i++) {
            numExpectedTrades += TradeSilo.tradeAmts.get(prof)[i]; // calculate the number of trades we expect villager to have
        }

        player.sendMessage(ChatColor.RED + "Num Expected Trades: " + numExpectedTrades + ". Trades size: " + trades.size());

        if(numExpectedTrades != trades.size()) { // if the number of expected trades doesn't match up with the number of trades (excluding upgrade one)...
            player.sendMessage(ChatColor.RED + "Size 1: " + trades.size());

            for(int i = 0; i < trades.size(); i++) {
                if (trades.get(i).getVillagerExperience() > 0) {
                    trades.remove(i); // we assume that the villager levelled up and so remove the ending trade.
                }
            }
            player.sendMessage(ChatColor.RED + "Size 2: " + trades.size());
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

            player.sendMessage(ChatColor.RED + "Size 3: " + trades.size());
            trades.add(TradeSilo.upgradeRecipe(exp)); // adding new final trade
            player.sendMessage(ChatColor.RED + "Size 4: " + trades.size());
        }

        int pLevel = (int) player.getHealth() / 4; // level of the player, not implemented yet so i assign it based on health
        player.sendMessage(ChatColor.RED + "Your current health is " + player.getHealth()  + "and your player level is" + pLevel + ". Villager level: " + exp);

        if(pLevel < exp - 1) { // pLevel goes from 0-5 and exp goes from 1-5
            int l = 0;
            for(int h = pLevel; h < exp - 1; h++) {
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
// At this point, I have the villager's profession, trades, and exp
// Next, I should remove all vanilla trades (those that involve emeralds) - DONE
// Then, check if trades.size() - 1 is the expected value in tradeAmts to see if it has the correct number of trades for its villager level. - DONE
// If the villager doesn't have the right number of trades, remove its last trade and add its new ones, adding a new last trade on.
// Seperately, check if the player is lower levelled than the villager.
// If so, cause all of that villager's trades to have a maxUses of their current uses.
// If not, cause the villager's trades to have a maxUses of 10.



}

