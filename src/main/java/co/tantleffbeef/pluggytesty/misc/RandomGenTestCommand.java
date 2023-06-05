package co.tantleffbeef.pluggytesty.misc;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

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

        @Override
        public String toString() {
            return "at: x:" + platformCenter.getBlockX() + " y:" + platformCenter.getBlockY() + " x:" + platformCenter.getBlockZ()
                    + "\n    direction: " + direction;
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
        final Material pathwayMaterial = Material.BLACK_CONCRETE;
        final Material optionalMaterial = Material.YELLOW_CONCRETE;

        final HashSet<Door> doors = new HashSet<>();
        final HashSet<Location> platforms = new HashSet<>();

        final ArrayList<Material> platformMaterialType = new ArrayList<>();

        // Fill platformMaterialType
        for (int i = 0; i < requiredNum; i++) {
            platformMaterialType.add(requiredMaterial);
        }

        for (int i = 0; i < optionalNum; i++) {
            platformMaterialType.add(optionalMaterial);
        }

        addPlatform(sender, platforms, doors, location, requiredMaterial);

        final var random = new Random();

        for (Material m : platformMaterialType) {
            final var door = doors.stream().skip(random.nextInt(doors.size())).findFirst().orElseThrow();

            sender.sendMessage("using door: " + door);

            final var platformLoc = door.platformCenter.clone().add(door.direction.getDirection().multiply(2));

            addPlatform(sender, platforms, doors, platformLoc, m);
        }

        sender.sendMessage("done i guess");
    }

    private static void addPlatform(CommandSender sender, HashSet<Location> platforms, HashSet<Door> doors, Location center, Material material) {
        sender.sendMessage("Adding platform at " + center.getBlockX() + " " + center.getBlockY() + " " + center.getBlockZ());

        buildPlatform(center, material);

        platforms.add(center);
        if (platforms.contains(center.clone().add(BlockFace.EAST.getDirection().multiply(3)))) {
            doors.remove(new Door(center.clone().add(BlockFace.EAST.getDirection().multiply(2)), BlockFace.WEST));
        } else {
            doors.add(new Door(center.clone().add(BlockFace.EAST.getDirection()), BlockFace.EAST));
        }

        if (platforms.contains(center.clone().add(BlockFace.WEST.getDirection().multiply(3)))) {
            doors.remove(new Door(center.clone().add(BlockFace.WEST.getDirection().multiply(2)), BlockFace.EAST));
        } else {
            doors.add(new Door(center.clone().add(BlockFace.WEST.getDirection()), BlockFace.WEST));
        }

        if (platforms.contains(center.clone().add(BlockFace.SOUTH.getDirection().multiply(3)))) {
            doors.remove(new Door(center.clone().add(BlockFace.SOUTH.getDirection().multiply(2)), BlockFace.NORTH));
        } else {
            doors.add(new Door(center.clone().add(BlockFace.SOUTH.getDirection()), BlockFace.SOUTH));
        }

        if (platforms.contains(center.clone().add(BlockFace.NORTH.getDirection().multiply(3)))) {
            doors.remove(new Door(center.clone().add(BlockFace.NORTH.getDirection().multiply(2)), BlockFace.SOUTH));
        } else {
            doors.add(new Door(center.clone().add(BlockFace.NORTH.getDirection()), BlockFace.NORTH));
        }
    }

    private static void buildPlatform(Location center, Material material) {
        final var middleX = center.getBlockX();
        final var middleY = center.getBlockY();
        final var middleZ = center.getBlockZ();

        final var world = center.getWorld();

        /*Bukkit.broadcastMessage("buildPlatform");
        Bukkit.broadcastMessage("middleX - 1: " + (middleX - 1));
        Bukkit.broadcastMessage("middleX + 1: " + (middleX + 1));
        Bukkit.broadcastMessage("middleY - 1: " + (middleY - 1));
        Bukkit.broadcastMessage("middleY + 1: " + (middleY + 1));
        Bukkit.broadcastMessage("middleZ - 1: " + (middleZ - 1));
        Bukkit.broadcastMessage("middleZ + 1: " + (middleZ + 1));*/

        for (int ix = middleX - 1; ix <= middleX + 1; ix++) {
            for (int iz = middleZ - 1; iz <= middleZ + 1; iz++) {
                // Bukkit.broadcastMessage("setting at " + ix + " " + middleY + " " + iz);
                new Location(world, ix, middleY, iz).getBlock().setType(material);
            }
        }
    }
}
