package co.tantleffbeef.pluggytesty.expeditions.loot;

import co.tantleffbeef.pluggytesty.attributes.AttributeManager;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class LootTableManager {

    private final AttributeManager attributeManager;
    public final Map<ExpeditionLootTables, ExpeditionLootTable> lootTables;

    public LootTableManager(AttributeManager attributeManager) {
        this.attributeManager = attributeManager;

        lootTables = new HashMap<>();

        for (ExpeditionLootTables table : ExpeditionLootTables.values()) {
            lootTables.put(table, getLootTable(table.getLocation()));
        }
    }

    private ExpeditionLootTable getLootTable(String location) {
        LootTableParser parsedJSON = new LootTableParser(location, attributeManager);
        return new ExpeditionLootTable(parsedJSON.getMinSlots(), parsedJSON.getMaxSlots(), parsedJSON.getLootPool());
    }

}
