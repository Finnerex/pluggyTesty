package co.tantleffbeef.pluggytesty.expeditions.loot;

import org.bukkit.loot.LootTable;

public enum ExpeditionLootTables {

    TIER_1_LOW("chests/tier_1/low_rarity"),

    ;

    private final ExpeditionLootTable lootTable;

    private ExpeditionLootTables(String location) {
        LootTableParser parsedJSON = new LootTableParser(location);
        this.lootTable = new ExpeditionLootTable(parsedJSON.getMinSlots(), parsedJSON.getMaxSlots(), parsedJSON.getLootPool());
    }

    public ExpeditionLootTable getLootTable() {
        return this.lootTable;
    }


}
