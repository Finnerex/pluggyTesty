package co.tantleffbeef.pluggytesty.misc;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;

@SuppressWarnings("unused")
@CommandAlias("visualize")
public class RandomGenTestCommand extends BaseCommand {
    private static class Door {
        public final Location location;
        public final BlockFace direction;

        public Door(Location location, BlockFace direction) {
            this.location = location;
            this.direction = direction;
        }

        @Override
        public String toString() {
            return "at: x:" + location.getBlockX() + " y:" + location.getBlockY() + " x:" + location.getBlockZ()
                    + "\n    direction: " + direction;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Door door = (Door) o;
            return location.equals(door.location) && direction == door.direction;
        }

        @Override
        public int hashCode() {
            return Objects.hash(location, direction);
        }
    }

    @Default
    public void onCommand(@NotNull Player sender) {
        onCommand(sender, 3, 3);
    }

    @Default
    public void onCommand(@NotNull Player sender, int requiredNum, int optionalNum) {
        final long startTime = System.nanoTime();

        final var location = sender.getLocation();
        final Material requiredMaterial = Material.CYAN_CONCRETE;
        final Material endMaterial = Material.RED_CONCRETE;
        final Material startMaterial = Material.GREEN_CONCRETE;
        final Material doorMaterial = Material.BLACK_CONCRETE;
        final Material optionalMaterial = Material.YELLOW_CONCRETE;

        final HashSet<Door> doors = new HashSet<>();
        final HashSet<Location> platforms = new HashSet<>();
        final ArrayList<Location> noDoorsRemovedLocations = new ArrayList<>();

        final ArrayList<Material> platformMaterialType = new ArrayList<>();

        // Fill platformMaterialType
        for (int i = 0; i < requiredNum; i++) {
            platformMaterialType.add(requiredMaterial);
        }

        for (int i = 0; i < optionalNum; i++) {
            platformMaterialType.add(optionalMaterial);
        }

        addPlatform(sender, platforms, doors, noDoorsRemovedLocations, location, startMaterial);

        final var random = new Random();

        for (Material m : platformMaterialType) {
            final var door = doors.stream().skip(random.nextInt(doors.size())).findFirst().orElseThrow();

            sender.sendMessage("using door: " + door);

            final var platformLoc = door.location.clone().add(door.direction.getDirection().multiply(2));

            addPlatform(sender, platforms, doors, noDoorsRemovedLocations, platformLoc, m);
        }

        // remove doors from the starting platform
        doors.remove(new Door(location.clone().add(1, 0, 0), BlockFace.EAST));
        doors.remove(new Door(location.clone().add(1, 0, 0), BlockFace.WEST));
        doors.remove(new Door(location.clone().add(0, 0, 1), BlockFace.SOUTH));
        doors.remove(new Door(location.clone().add(0, 0, -1), BlockFace.NORTH));

        final var door = doors.stream().skip(random.nextInt(doors.size())).findFirst().orElseThrow();

        sender.sendMessage("using door: " + door);

        final var endPlatformLoc = door.location.clone().add(door.direction.getDirection().multiply(2));

        addPlatform(sender, platforms, doors, noDoorsRemovedLocations, endPlatformLoc, endMaterial);

        // show unused doors
        sender.sendMessage("creating unused doors");
        for (Door d : doors) {
            final var doorLocation = d.location;

            sender.sendMessage(doorLocation.getBlockX() + ", " + doorLocation.getBlockY() + ", " + doorLocation.getBlockZ());

            doorLocation.getBlock().setType(doorMaterial);
        }

        for (Location l : noDoorsRemovedLocations) {
            sender.sendMessage(ChatColor.RED + "doorsRemoved < 1 at " + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ());
        }

        final long endTime = System.nanoTime();
        final double duration = (endTime - startTime) / 1000000.0;

        System.out.printf("done I guess (%.3f ms)", duration);
    }

    private static void addPlatform(CommandSender sender, HashSet<Location> platforms, HashSet<Door> doors, ArrayList<Location> noDoorsRemovedLocations, Location center, Material material) {
        sender.sendMessage("Adding platform at " + center.getBlockX() + " " + center.getBlockY() + " " + center.getBlockZ());

        buildPlatform(center, material);

        int doorsRemoved = 0;

        platforms.add(center);
        if (platforms.contains(center.clone().add(3, 0, 0))) {
            sender.sendMessage("removing door to the EAST");
            doors.remove(new Door(center.clone().add(BlockFace.EAST.getDirection().multiply(2)), BlockFace.WEST));
            doorsRemoved++;
        } else {
            sender.sendMessage("adding door to the EAST");
            doors.add(new Door(center.clone().add(1, 0, 0), BlockFace.EAST));
        }

        if (platforms.contains(center.clone().add(BlockFace.WEST.getDirection().multiply(3)))) {
            sender.sendMessage("removing door to the WEST");
            doors.remove(new Door(center.clone().add(BlockFace.WEST.getDirection().multiply(2)), BlockFace.EAST));
            doorsRemoved++;
        } else {
            sender.sendMessage("adding door to the WEST");
            doors.add(new Door(center.clone().add(BlockFace.WEST.getDirection()), BlockFace.WEST));
        }

        if (platforms.contains(center.clone().add(BlockFace.SOUTH.getDirection().multiply(3)))) {
            sender.sendMessage("removing door to the SOUTH");
            doors.remove(new Door(center.clone().add(BlockFace.SOUTH.getDirection().multiply(2)), BlockFace.NORTH));
            doorsRemoved++;
        } else {
            sender.sendMessage("adding door to the SOUTH");
            doors.add(new Door(center.clone().add(BlockFace.SOUTH.getDirection()), BlockFace.SOUTH));
        }

        if (platforms.contains(center.clone().add(BlockFace.NORTH.getDirection().multiply(3)))) {
            sender.sendMessage("removing door to the NORTH");
            doors.remove(new Door(center.clone().add(BlockFace.NORTH.getDirection().multiply(2)), BlockFace.SOUTH));
            doorsRemoved++;
        } else {
            sender.sendMessage("adding door to the NORTH");
            doors.add(new Door(center.clone().add(BlockFace.NORTH.getDirection()), BlockFace.NORTH));
        }

        if (doorsRemoved < 1)
            noDoorsRemovedLocations.add(center);
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
