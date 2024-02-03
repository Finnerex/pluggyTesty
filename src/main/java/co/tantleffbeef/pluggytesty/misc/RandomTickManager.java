package co.tantleffbeef.pluggytesty.misc;

import co.tantleffbeef.pluggytesty.expeditions.loot.RandomCollection;
import org.bukkit.plugin.Plugin;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RandomTickManager {

    private final RandomCollection<RandomTicker> tickers = new RandomCollection<>(); // things will be more likely if there are fewer of them

    public RandomTickManager(int ptRandomTickInterval) {

        final Random random = new Random();

        // umm idk if this is the way I should be doing this
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(
                () -> {
                    if (random.nextBoolean()) // idk man this is sort of a bad system
                        tickers.next().onRandomTick();
                },
                0, ptRandomTickInterval * 50L, TimeUnit.MILLISECONDS);

    }

    public void registerRandomTicker(RandomTicker ticker, double weight) {
        tickers.add(weight, ticker);
    }

}
