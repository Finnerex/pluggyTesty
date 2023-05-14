package co.tantleffbeef.pluggytesty.expeditions.loot;

import co.tantleffbeef.pluggytesty.PluggyTesty;
import com.google.gson.stream.JsonReader;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import co.tantleffbeef.mcplanes.Tools

import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileReader;
import java.io.IOException;

public class LootTableParser {

    // should be final but can't set a final var in a loop
    private int minSlots;
    private int maxSlots;
    private final RandomCollection<ItemStack> lootPool;

    public LootTableParser(String location) {
        // path to the loot table in question
        var path = JavaPlugin.getPlugin(PluggyTesty.class).getResource("data/pluggytesty/loot_tables/" + location);

        lootPool = new RandomCollection<>();

        if (path == null)
            return;

        try (JsonReader reader = new JsonReader(new FileReader(path.toString()))) {

            // chest are arrays where indices are the rarity
            reader.beginObject();

            // 2 because named with rarity, starting another object
            reader.beginObject();

            // loop through whole file
            while (reader.hasNext()) {
                String name = reader.nextName();

                // ignore rarity name
//                if (name.contains("rarity"))
//                    continue;

                if (name.equals("min_slots"))
                    this.minSlots = reader.nextInt();

                else if (name.equals("max_slots"))
                    this.maxSlots = reader.nextInt();

                else if (name.equals("loot_pool")) {
                    reader.beginArray();

                    while (reader.hasNext()) {
                        // each element in the array is an object
                        reader.beginObject();

                        // this ignores the names, maybe it shouldn't?
                        final int weight = reader.nextInt();
                        final String type = reader.nextString();
                        final int amount = reader.nextInt();

                        final ItemStack itemStack = new ItemStack(Material.ACACIA_FENCE, amount);

                        lootPool.add(weight, itemStack);

                        reader.endObject();
                    }

                    reader.endArray(); // end of loot pool

                }

            }

            reader.endObject();
            reader.endObject();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int getMinSlots() { return minSlots; }
    public int getMaxSlots() { return maxSlots; }
    public RandomCollection<ItemStack> getLootPool() { return lootPool; }

}
