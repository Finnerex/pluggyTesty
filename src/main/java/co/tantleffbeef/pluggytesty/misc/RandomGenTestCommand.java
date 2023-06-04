package co.tantleffbeef.pluggytesty.misc;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@CommandAlias("visualize")
public class RandomGenTestCommand extends BaseCommand {
    private static class Door {
        public final Location platformCenter;
        public final BlockFace direction;

        public Door(Location platformCenter, BlockFace direction) {
            this.platformCenter = platformCenter;
            this.direction = direction;
        }
    }

    @Default
    public void onCommand(@NotNull Player sender) {
        onCommand(sender, 3, 3);
    }

    @Default
    public void onCommand(@NotNull Player sender, int requiredNum, int optionalNum) {
        final var location = sender.getLocation();
        final Material requiredMaterial = Material.CYAN_CONCRETE;
        final Material endMaterial = Material.RED_CONCRETE;
        final Material startMaterial = Material.GREEN_CONCRETE;
        final Material pathwayMaterial = Material.YELLOW_CONCRETE;

        buildPlatform(location, requiredMaterial);
        sender.sendMessage("done i guess");
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
