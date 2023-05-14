package co.tantleffbeef.pluggytesty.expeditions.loot;

import org.bukkit.Bukkit;
import org.bukkit.loot.LootTable;

public enum ExpeditionLootTables {

    TIER_1_LOW("chests/tier_1/low_rarity"),

    ;

    private final ExpeditionLootTable lootTable;

    ExpeditionLootTables(String location) {
        LootTableParser parsedJSON = new LootTableParser(location);
        Bukkit.broadcastMessage("This happened (it won't)");
        this.lootTable = new ExpeditionLootTable(parsedJSON.getMinSlots(), parsedJSON.getMaxSlots(), parsedJSON.getLootPool());
    }

    public ExpeditionLootTable getLootTable() {
        return this.lootTable;
    }


}
