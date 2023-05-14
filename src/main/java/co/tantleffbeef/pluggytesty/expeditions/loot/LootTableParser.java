package co.tantleffbeef.pluggytesty.expeditions.loot;

import co.tantleffbeef.pluggytesty.PluggyTesty;
import com.google.gson.stream.JsonReader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LootTableParser {

    // should be final but can't set a final var in a loop
    private int minSlots;
    private int maxSlots;
    private final RandomCollection<ItemStack> lootPool;

    public LootTableParser(String location) {
        // path to the loot table in question
        var path = JavaPlugin.getPlugin(PluggyTesty.class)
                .getResource(/*"data/pluggytesty/loot_tables/" + location + ".json"*/
                "data/loot_tables/chests/tier_1/low_rarity.json");


        lootPool = new RandomCollection<>();

        Bukkit.broadcastMessage("path gotten");

        if (path == null)
            return;

        Bukkit.broadcastMessage("path not null");


        try (JsonReader reader = new JsonReader(new InputStreamReader(path))) {

            Bukkit.broadcastMessage("try");

            // chest are arrays where indices are the rarity
            reader.beginObject();

            // 2 because named with rarity, starting another object
//            reader.beginObject();

            // loop through whole file
            while (reader.hasNext()) {
                String name = reader.nextName();

                // ignore rarity name
//                if (name.contains("rarity"))
//                    continue;

                switch (name) {
                    case "min_slots" -> {
                        this.minSlots = reader.nextInt();
                        Bukkit.broadcastMessage("min slots: " + minSlots);
                    }
                    case "max_slots" -> {
                        this.maxSlots = reader.nextInt();
                        Bukkit.broadcastMessage("max slots: " + maxSlots);
                    }
                    case "loot_pool" -> {

                        reader.beginArray();

                        while (reader.hasNext()) {
                            // each element in the array is an object
                            reader.beginObject();

                            Bukkit.broadcastMessage("\nloot pool item: ");

                            // this ignores the names, maybe it shouldn't?
                            assert reader.nextName().equals("weight");
                            final int weight = reader.nextInt();

                            assert reader.nextName().equals("type");
                            final String type = reader.nextString();

                            assert reader.nextName().equals("amount");
                            final int amount = reader.nextInt();

                            Bukkit.broadcastMessage("  weight: " + weight + "\n  type: " + type + "\n  amount: " + amount);

                            final ItemStack itemStack = new ItemStack(Material.ACACIA_FENCE, amount);

                            lootPool.add(weight, itemStack);

                            reader.endObject();
                        }

                        reader.endArray(); // end of loot pool
                    }
                }

            }

//            reader.endObject();
            reader.endObject();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int getMinSlots() { return minSlots; }
    public int getMaxSlots() { return maxSlots; }
    public RandomCollection<ItemStack> getLootPool() { return lootPool; }

}
