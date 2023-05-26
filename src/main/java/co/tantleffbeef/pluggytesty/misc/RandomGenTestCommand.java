package co.tantleffbeef.pluggytesty.misc;

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

        for (int ix = middleX - 1; ix <= middleX + 1; ix++) {
            for (int iy = middleY - 1; iy <= middleY + 1; iy++) {
                for (int iz = middleY - 1; iz <= middleZ + 1; iz++) {
                    new Location(world, ix, iy, iz).getBlock().setType(material);
                }
            }
        }
    }
}
