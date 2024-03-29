package co.tantleffbeef.pluggytesty.expeditions.loot;

import co.tantleffbeef.pluggytesty.attributes.AttributeManager;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.loot.LootContext;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class LootTableTestCommand implements CommandExecutor {

    private final Plugin namespace;
    private final LootTableManager lootTableManager;
    ReplenishableChestManager replenishableChestManager;

    public LootTableTestCommand(Plugin namespace, LootTableManager lootTableManager, ReplenishableChestManager replenishableChestManager) {
        this.namespace = namespace;
        this.lootTableManager = lootTableManager;
        this.replenishableChestManager = replenishableChestManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player))
            return false;


        // make the loot table (for real later)
//        LootTable lootTable = Bukkit.getLootTable(new NamespacedKey("pluggy_testy", "loot_tables/chests/chest_test"));
//        LootTable lootTable = Bukkit.getLootTable(NamespacedKey.minecraft("chests/village/village_armorer"));
//        LootTable lootTable = LootTables.VILLAGE_ARMORER.getLootTable();
//        LootTable lootTable = new ExpeditionLootTable(namespace);
//        ExpeditionLootTable lootTable = ExpeditionLootTables.TIER_1_LOW.getLootTable();
        ExpeditionLootTable lootTable = lootTableManager.get(ExpeditionLootTables.TIER_1_LOW);


        if (lootTable == null) {
            Bukkit.broadcastMessage("that thang was null");
            return false;
        }

        RayTraceResult result = player.rayTraceBlocks(5);

        if (result == null) {
            Bukkit.broadcastMessage("result null");
            return false;
        }

        Block block = result.getHitBlock();

        /*block == null || block.getType() != Material.CHEST*/
        if (!(block.getState() instanceof Chest chest)) {
            Bukkit.broadcastMessage("not a chest");
            return false;
        }

        Bukkit.broadcastMessage("set");

        Random r = new Random();

        if (strings.length < 1 || strings[0].equals("r"))
            replenishableChestManager.registerReplenishableChest(chest.getInventory(), ExpeditionLootTables.TIER_1_LOW);
        else
            lootTable.fillInventory(chest.getInventory(), new Random(r.nextLong()), buildContext(player));

        return true;
    }

    private LootContext buildContext(Player p) {
        LootContext.Builder builder = new LootContext.Builder(p.getLocation());
        builder.luck(100);
        return builder.build();
    }

}
