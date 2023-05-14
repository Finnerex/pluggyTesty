package co.tantleffbeef.pluggytesty.expeditions.loot;

public enum ExpeditionLootTables {

    TIER_1_LOW("chests/tier_1/low_rarity"),

    ;

    private final String location;

    ExpeditionLootTables(String location) { this.location = location; }

    public String getLocation() { return this.location; }


}
