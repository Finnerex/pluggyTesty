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

    // weight, material, max amount
    private final RandomCollection<Map.Entry<Material, Integer>> lootPool = new RandomCollection<Map.Entry<Material, Integer>>()
            .add(200, entry(Material.CARROT, 5))
            .add(40, entry(Material.BREAD, 60))
            .add(1, entry(Material.DIAMOND, 1));
    private int totalWeight;

    public T1Low(Plugin namespace) {
        this.namespace = namespace;
    }

    @NotNull
    @Override
    public Collection<ItemStack> populateLoot(@Nullable Random random, @NotNull LootContext lootContext) {
        Collection<ItemStack> lootOut = new ArrayList<>();

        if (random == null)
            return lootOut;

        int numSlots = random.nextInt(5, 15); // number of slots to be filled

        for (int i = 0; i < numSlots; i++) {

        }
    }

    @Override
    public void fillInventory(@NotNull Inventory inventory, @Nullable Random random, @NotNull LootContext lootContext) {

    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(namespace, "tier_1_low");
    }
}
