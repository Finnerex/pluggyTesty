package co.tantleffbeef.pluggytesty.misc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class RandomGenTestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Entity e))
            return false;

        final var location = e.getLocation();
        final Material requiredMaterial = Material.CYAN_CONCRETE;
        final Material optionalEndMaterial = Material.RED_CONCRETE;
        final Material optionalPathwayMaterial = Material.YELLOW_CONCRETE;

        buildPlatform(location, requiredMaterial);
        e.sendMessage("done i guess");
        return true;
    }

    private static void buildPlatform(Location center, Material material) {
        final var middleX = center.getBlockX();
        final var middleY = center.getBlockY();
        final var middleZ = center.getBlockZ();

        final var world = center.getWorld();

        Bukkit.broadcastMessage("buildPlatform");
        Bukkit.broadcastMessage("middleX - 1: " + (middleX - 1));
        Bukkit.broadcastMessage("middleX + 1: " + (middleX + 1));
        Bukkit.broadcastMessage("middleY - 1: " + (middleY - 1));
        Bukkit.broadcastMessage("middleY + 1: " + (middleY + 1));
        Bukkit.broadcastMessage("middleZ - 1: " + (middleZ - 1));
        Bukkit.broadcastMessage("middleZ + 1: " + (middleZ + 1));

        for (int ix = middleX - 1; ix <= middleX + 1; ix++) {
            for (int iz = middleZ - 1; iz <= middleZ + 1; iz++) {
                Bukkit.broadcastMessage("setting at " + ix + " " + middleY + " " + iz);
                new Location(world, ix, middleY, iz).getBlock().setType(material);
            }
        }
    }
}
