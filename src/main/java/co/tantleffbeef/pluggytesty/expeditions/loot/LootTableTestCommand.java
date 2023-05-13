package co.tantleffbeef.pluggytesty.expeditions.loot;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.loot.LootTable;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class LootTableTestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player))
            return false;


        // make the loot table (for real later)
        LootTable lootTable = Bukkit.getLootTable(new NamespacedKey("pluggy_testy", "chests/chest_test"));

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
        Bukkit.broadcastMessage("block " + block);

        /*block == null || block.getType() != Material.CHEST*/
        if (!(block.getState() instanceof Chest chest)) {
            Bukkit.broadcastMessage("not a chest");
            return false;
        }

        Bukkit.broadcastMessage("set");
        chest.setLootTable(lootTable);

        return true;
    }

}
