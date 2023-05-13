package co.tantleffbeef.pluggytesty.expeditions.loot;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import static java.util.Map.entry;

import java.util.*;

public class T1Low implements LootTable {
    private final Plugin namespace;

    // weight, material, itemStack amount = max amount
    private final RandomCollection<ItemStack> lootPool = new RandomCollection<ItemStack>()
            .add(200, new ItemStack(Material.CARROT, 5))
            .add(40, new ItemStack(Material.BREAD, 64))
            .add(1, new ItemStack(Material.DIAMOND, 1));

    public T1Low(Plugin namespace) {
        this.namespace = namespace;
    }

    @NotNull
    @Override
    public Collection<ItemStack> populateLoot(@Nullable Random random, @NotNull LootContext lootContext) {
        Collection<ItemStack> loot = new ArrayList<>();

        if (random == null)
            return loot;

        int numSlots = random.nextInt(5, 15); // number of slots to be filled

        for (int i = 0; i < numSlots; i++) {
            ItemStack item = lootPool.next();
            item.setAmount(random.nextInt(item.getAmount()) + 1);
            loot.add(item);
        }

        return loot;
    }

    @Override
    public void fillInventory(@NotNull Inventory inventory, @Nullable Random random, @NotNull LootContext lootContext) {
        Collection<ItemStack> loot = populateLoot(random, lootContext);

        if (random == null)
            return;

        for (ItemStack i : loot) {
            int slot;
            do {
                slot = random.nextInt(inventory.getSize());
            } while (inventory.getItem(slot) == null);

            loot.remove(i);
            inventory.addItem(i);
        }

    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(namespace, "tier_1_low");
    }
}
