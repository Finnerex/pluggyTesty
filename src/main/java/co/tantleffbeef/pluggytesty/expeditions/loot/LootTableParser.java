package co.tantleffbeef.pluggytesty.expeditions.loot;

import co.tantleffbeef.pluggytesty.PluggyTesty;
import co.tantleffbeef.pluggytesty.attributes.AttributeManager;
import co.tantleffbeef.pluggytesty.attributes.InvalidItemKeyException;
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

    public LootTableParser(String location, AttributeManager attributeManager) {
        // path to the loot table in question
        var path = JavaPlugin.getPlugin(PluggyTesty.class)
                .getResource("data/loot_tables/" + location + ".json");


        lootPool = new RandomCollection<>();

        Bukkit.broadcastMessage("path gotten");

        if (path == null)
            return;

        Bukkit.broadcastMessage("path not null");


        try (JsonReader reader = new JsonReader(new InputStreamReader(path))) {

            Bukkit.broadcastMessage("try");

            // chest are arrays where indices are the rarity
            reader.beginObject();

            // loop through whole file
            while (reader.hasNext()) {
                String name = reader.nextName();

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

                            // check to make sure correct syntax
                            checkName(reader.nextName(), "weight");
                            final int weight = reader.nextInt();

                            checkName(reader.nextName(), "type");
                            final String type = reader.nextString();

                            checkName(reader.nextName(), "amount");
                            final int amount = reader.nextInt();

                            final NamespacedKey itemKey = NamespacedKey.fromString(type);

                            if (itemKey == null)
                                throw new RuntimeException("Invalid item");

                            final ItemStack itemStack = attributeManager.defaultItemStack(itemKey);
                            itemStack.setAmount(amount);

                            lootPool.add(weight, itemStack);

                            reader.endObject();
                        }

                        reader.endArray(); // end of loot pool
                    }
                }

            }

            reader.endObject();

        } catch (IOException | InvalidItemKeyException e) {
            e.printStackTrace();
        }

    }

    private void checkName(String name, String valid) {
        if (!name.equals(valid))
            throw new RuntimeException("Invalid Loot Table Syntax, should '" + name + "' be '" + valid + "'?");
    }

    public int getMinSlots() { return minSlots; }
    public int getMaxSlots() { return maxSlots; }
    public RandomCollection<ItemStack> getLootPool() { return lootPool; }

}
