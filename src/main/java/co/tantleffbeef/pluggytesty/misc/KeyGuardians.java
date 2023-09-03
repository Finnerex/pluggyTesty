package co.tantleffbeef.pluggytesty.misc;
import org.bukkit.Bukkit;

import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Calendar;
import java.util.Random;
import org.bukkit.block.Biome;
import static java.util.Map.entry;

public class KeyGuardians {
    public static Map<String, Biome> dailySpawns = new HashMap<>();
    public KeyGuardians() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/New_York"));
        ZonedDateTime nextRun = now.plusDays(1).withHour(0).withMinute(0).withSecond(0);

        long initialDelay = Duration.between(now, nextRun).getSeconds();
        Executors.newScheduledThreadPool(1).schedule(shuffleSpawns, 0, TimeUnit.SECONDS);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(shuffleSpawns,
            initialDelay,
            TimeUnit.DAYS.toSeconds(1),
            TimeUnit.SECONDS);
    }

    private Runnable shuffleSpawns = new Runnable() {
        @Override
        public void run() {
            // use RNG to
            long today = ZonedDateTime.now(ZoneId.of("America/New_York")).toLocalDate().toEpochDay();
            dailySpawns.clear();
            Random rng = new Random(today);
             int sLevel = 6; // edit this later to incorporate server's level
//            switch(sLevel) { // these values could change later
//                case 1: sLevel = 4;
//                case 2: sLevel = 9;
//                case 3: sLevel = 14;
//                case 4: sLevel = 19;
//                case 5: sLevel = 23;
//                case 6: sLevel = 24;
//                default: sLevel = 0;
//            }
            String[] guardians = { // might need to remove some expeditions from this list later
                    "ZombieExpKeyGuardian", "SkeletonExpKeyGuardian", "ZombifiedPiglinExpKeyGuardian", "UndeadTrialKeyGuardian",
                    "EarthExpKeyGuardian", "WaterExpKeyGuardian", "IceExpKeyGuardian", "AirExpKeyGuardian", "WeatherTrialKeyGuardian",
                    "NorthOutpostExpKeyGuardian", "EastOutpostExpKeyGuardian", "SouthOutpostExpKeyGuardian", "WestOutpostExpKeyGuardian", "MansionTrialKeyGuardian",
                    "LavaExpKeyGuardian", "SoulExpKeyGuardian", "ScorchedExpKeyGuardian", "PiglinExpKeyGuardian", "MagmaTrialKeyGuardian",
                    "WastelandExpKeyGuardian", "DarknessExpKeyGuardian", "HappyAnimalsExpKeyGuardian", "DecayTrialKeyGuardian",
                    "FinalTrialKeyGuardian"
            };

            for(int i = 0; i < sLevel; i++) {
                Biome biome = Biome.values()[rng.nextInt(sLevel + 1)];
                if(dailySpawns.containsValue(biome)) {
                    i--;
                } else {
                    dailySpawns.put(guardians[i], biome);
                }

            }


        }
    };

}
