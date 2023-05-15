package co.tantleffbeef.pluggytesty.expeditions.loot;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ExpeditionLootTable {
    // weight, material, itemStack amount = max amount
    private final RandomCollection<ItemStack> lootPool;
    private final int minSlots;
    private final int maxSlots;

    public ExpeditionLootTable(int minSlots, int maxSlots, RandomCollection<ItemStack> lootPool) {
        this.minSlots = minSlots;
        this.maxSlots = maxSlots;
        this.lootPool = lootPool;
    }

    @NotNull
    public Collection<ItemStack> populateLoot(@Nullable Random random, @NotNull LootContext lootContext) {
        Collection<ItemStack> loot = new ArrayList<>();

        if (random == null)
            return loot;

        Bukkit.broadcastMessage("min: " + minSlots + "\nmax: " + maxSlots);
        int numSlots = random.nextInt(minSlots, maxSlots); // number of slots to be filled

        for (int i = 0; i < numSlots; i++) {
            ItemStack item = lootPool.next().clone();
            item.setAmount(random.nextInt(item.getAmount()) + 1);
            loot.add(item);
        }

        return loot;
    }

    public void fillInventory(@NotNull Inventory inventory, @Nullable Random random, @NotNull LootContext lootContext) {
        Collection<ItemStack> loot = populateLoot(random, lootContext);

        if (random == null)
            return;

        for (ItemStack i : loot) {

            int slot = random.nextInt(inventory.getSize());

            inventory.setItem(slot, i);
        }

    }

}
