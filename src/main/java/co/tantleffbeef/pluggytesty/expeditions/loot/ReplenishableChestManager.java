package co.tantleffbeef.pluggytesty.expeditions.loot;

import co.tantleffbeef.pluggytesty.misc.RandomTicker;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class ReplenishableChestManager implements RandomTicker {


    private final LootTableManager tableManager;
    private final ArrayList<Map.Entry<Inventory, ExpeditionLootTable>> chests = new ArrayList<>();
    private final Random random = new Random();

    public ReplenishableChestManager(LootTableManager tableManager) {
        this.tableManager = tableManager;
    }

    public void registerReplenishableChest(Inventory chest, ExpeditionLootTables table) {
        chests.add(Map.entry(chest, tableManager.get(table)));
    }

    @Override
    public void onRandomTick() {
        for (Map.Entry<Inventory, ExpeditionLootTable> chest : chests) {
            Inventory inv = chest.getKey();
            int slot = random.nextInt(inv.getSize());

            if (inv.getItem(slot) != null)
                inv.setItem(slot, chest.getValue().next(random));

        }
    }
}
