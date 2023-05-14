package co.tantleffbeef.pluggytesty.expeditions.loot;

import org.bukkit.Bukkit;
import org.bukkit.loot.LootTable;

public enum ExpeditionLootTables {

    TIER_1_LOW("chests/tier_1/low_rarity"),

    ;
    //// https://discord.com/channels/@me/1089055393327611978/1107380228042534973

    private final ExpeditionLootTable lootTable;

    ExpeditionLootTables(String location) {
        LootTableParser parsedJSON = new LootTableParser(location);
        Bukkit.broadcastMessage("This happened (it won't)"); ////// IT DID!!!
        this.lootTable = new ExpeditionLootTable(parsedJSON.getMinSlots(), parsedJSON.getMaxSlots(), parsedJSON.getLootPool());
    }

    public ExpeditionLootTable getLootTable() {
        return this.lootTable;
    }


}
