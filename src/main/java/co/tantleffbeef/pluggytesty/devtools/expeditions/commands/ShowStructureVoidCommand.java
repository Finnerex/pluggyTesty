package co.tantleffbeef.pluggytesty.devtools.expeditions.commands;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ShowStructureVoidCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(ChatColor.RED + "This command must be run by a player!");
            return true;
        }

        try {
            // Grab the player's world
            final var bukkitWorld = p.getWorld();
            final var weWorld = BukkitAdapter.adapt(bukkitWorld);

            // grab the worldedit version of the player
            final var wePlayer = BukkitAdapter.adapt(p);

            // Tries to grab the selection, this will throw
            // an error if there isn't one which we catch
            final var selection = wePlayer.getSession().getSelection(weWorld);

            final var changedBlocks = new ArrayList<BlockState>();

            // loop through blocks and find structure voids
            selection.forEach((blockPos) -> {
                final var block =
                        new Location(bukkitWorld, blockPos.getBlockX(), blockPos.getBlockY(), blockPos.getBlockZ())
                                .getBlock();

                blockPos.getBlock(weWorld);

                if (block.getType() != Material.STRUCTURE_VOID)
                    return;

                final var modifiedState = block.getState();

                modifiedState.setType(Material.RED_STAINED_GLASS);

                changedBlocks.add(modifiedState);
            });

            p.sendBlockChanges(changedBlocks, false);
        } catch (IncompleteRegionException e) {
            p.sendMessage(ChatColor.RED + "You aren't selecting anything!");
            return false;
        }

        return true;
    }
}
