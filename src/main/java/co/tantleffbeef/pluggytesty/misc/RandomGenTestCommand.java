package co.tantleffbeef.pluggytesty.misc;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

@SuppressWarnings("unused")
@CommandAlias("visualize")
public class RandomGenTestCommand extends BaseCommand implements Runnable {
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

    private static class LocationMaterialPair {
        public final Location location;
        public final Material material;

        public LocationMaterialPair(Location location, Material material) {
            this.location = location;
            this.material = material;
        }
    }

    private final BukkitScheduler scheduler;
    private final Plugin schedulerPlugin;
    private final ArrayList<LocationMaterialPair> pairsToPlace = new ArrayList<>(4000);
    private final ArrayList<Location> blackConcreteBlocksToPlace = new ArrayList<>();
    private int placePerTick;

    public RandomGenTestCommand(BukkitScheduler scheduler, Plugin schedulerPlugin, int placePerTick) {
        this.scheduler = scheduler;
        this.schedulerPlugin = schedulerPlugin;
        this.placePerTick = placePerTick;
    }

    @Override
    public void run() {
        if (pairsToPlace.size() == 0 && blackConcreteBlocksToPlace.size() == 0)
            return;

        synchronized (pairsToPlace) {
            for (int i = pairsToPlace.size() - 1; i >= Math.max(0, pairsToPlace.size() - placePerTick); i--) {
                final var pair = pairsToPlace.get(i);
                final var center = pair.location;
                final var material = pair.material;

                final var middleX = center.getBlockX();
                final var middleY = center.getBlockY();
                final var middleZ = center.getBlockZ();

                final var world = center.getWorld();

                for (int ix = middleX - 1; ix <= middleX + 1; ix++) {
                    for (int iz = middleZ - 1; iz <= middleZ + 1; iz++) {
                        // Bukkit.broadcastMessage("setting at " + ix + " " + middleY + " " + iz);
                        new Location(world, ix, middleY, iz).getBlock().setType(material);
                    }
                }

                pairsToPlace.remove(i);
            }
        }

        synchronized (blackConcreteBlocksToPlace) {
            for (int i = blackConcreteBlocksToPlace.size() - 1; i >= Math.max(0, blackConcreteBlocksToPlace.size() - placePerTick); i--) {
                final var location = blackConcreteBlocksToPlace.get(i);

                location.getBlock().setType(Material.BLACK_CONCRETE);

                blackConcreteBlocksToPlace.remove(i);
            }
        }
    }

    @Subcommand("set")
    public void onSet(@NotNull CommandSender sender, int toSetTo) {
        placePerTick = toSetTo;
        sender.sendMessage("Set placePerTick to " + toSetTo);
    }

    @Default
    public void onCommand(@NotNull Player sender) {
        onCommand(sender, 3, 3);
    }

    @Default
    public void onCommand(@NotNull Player sender, int requiredNum, int optionalNum) {
        onCommand(sender, requiredNum, optionalNum, new Random().nextInt());
    }

    @Default
    public void onCommand(@NotNull Player sender, int requiredNum, int optionalNum, int seed) {
        scheduler.runTaskAsynchronously(schedulerPlugin, () -> onCommandAsync(sender, requiredNum, optionalNum, seed));
    }

    private void onCommandAsync(@NotNull Player sender, int requiredNum, int optionalNum, int seed) {
        final long startTime = System.nanoTime();

        final var location = sender.getLocation().subtract(0, 1, 0);
        final Material requiredMaterial = Material.CYAN_CONCRETE;
        final Material endMaterial = Material.RED_CONCRETE;
        final Material startMaterial = Material.GREEN_CONCRETE;
        final Material doorMaterial = Material.BLACK_CONCRETE;
        final Material optionalMaterial = Material.YELLOW_CONCRETE;

        final ArrayList<Door> doors = new ArrayList<>();
        final ArrayList<Location> platforms = new ArrayList<>();
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

        final var random = new Random(seed);

        while (platformMaterialType.size() > 0) {
            final int mNum = random.nextInt(platformMaterialType.size()) ;
            final Material m = platformMaterialType.get(mNum);
            platformMaterialType.remove(mNum);

            final var door = doors.stream().skip(random.nextInt(doors.size())).findFirst().orElseThrow();

            sender.sendMessage("using door: " + door);

            final var platformLoc = door.location.clone().add(door.direction.getDirection().multiply(2));

            addPlatform(sender, platforms, doors, noDoorsRemovedLocations, platformLoc, m);
        }

        // remove doors from the starting platform
        doors.remove(new Door(location.clone().add(1, 0, 0), BlockFace.EAST));
        doors.remove(new Door(location.clone().add(-1, 0, 0), BlockFace.WEST));
        doors.remove(new Door(location.clone().add(0, 0, 1), BlockFace.SOUTH));
        doors.remove(new Door(location.clone().add(0, 0, -1), BlockFace.NORTH));

        final var door = doors.stream().skip(random.nextInt(doors.size())).findFirst().orElseThrow();

        sender.sendMessage("using door: " + door);

        final var endPlatformLoc = door.location.clone().add(door.direction.getDirection().multiply(2));

        addPlatform(sender, platforms, doors, noDoorsRemovedLocations, endPlatformLoc, endMaterial);

        // show unused doors
        sender.sendMessage("creating unused doors");

        synchronized (blackConcreteBlocksToPlace) {
            for (Door d : doors) {
                final var doorLocation = d.location;

                sender.sendMessage(doorLocation.getBlockX() + ", " + doorLocation.getBlockY() + ", " + doorLocation.getBlockZ());

                blackConcreteBlocksToPlace.add(doorLocation);
            }
        }

        for (Location l : noDoorsRemovedLocations) {
            final var clickText = new TextComponent("doorsRemoved < 1 at " + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ());
            clickText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + l.getBlockX() + " " + (l.getBlockY() + 1) + " " + l.getBlockZ()));

            sender.spigot().sendMessage(new ComponentBuilder(clickText).color(ChatColor.RED).create());
        }

        final long endTime = System.nanoTime();
        final double duration = (endTime - startTime) / 1000000.0;

        sender.sendMessage(ChatColor.GREEN + "seed: " + seed);
        final var completeMessage = String.format("done I guess (%.2fk required, %.2fk optional, %.3fms)", requiredNum / 1000.0, optionalNum / 1000.0, duration);
        sender.sendMessage(completeMessage);
        Debug.alwaysInfo(completeMessage);
    }

    private void addPlatform(CommandSender sender, ArrayList<Location> platforms, ArrayList<Door> doors, ArrayList<Location> noDoorsRemovedLocations, Location center, Material material) {
        sender.sendMessage("Adding platform at " + center.getBlockX() + " " + center.getBlockY() + " " + center.getBlockZ());

        buildPlatform(center, material);

        int doorsRemoved = 0;

        platforms.add(center);
        if (platforms.contains(center.clone().add(3, 0, 0))) {
            sender.sendMessage("removing door to the EAST");
            doors.remove(new Door(center.clone().add(2, 0, 0), BlockFace.WEST));
            doorsRemoved++;
        } else {
            sender.sendMessage("adding door to the EAST");
            doors.add(new Door(center.clone().add(1, 0, 0), BlockFace.EAST));
        }

        if (platforms.contains(center.clone().add(-3, 0, 0))) {
            sender.sendMessage("removing door to the WEST");
            doors.remove(new Door(center.clone().add(-2, 0, 0), BlockFace.EAST));
            doorsRemoved++;
        } else {
            sender.sendMessage("adding door to the WEST");
            doors.add(new Door(center.clone().add(-1, 0, 0), BlockFace.WEST));
        }

        if (platforms.contains(center.clone().add(0, 0, 3))) {
            sender.sendMessage("removing door to the SOUTH");
            doors.remove(new Door(center.clone().add(0, 0, 2), BlockFace.NORTH));
            doorsRemoved++;
        } else {
            sender.sendMessage("adding door to the SOUTH");
            doors.add(new Door(center.clone().add(0, 0, 1), BlockFace.SOUTH));
        }

        if (platforms.contains(center.clone().add(0, 0, -3))) {
            sender.sendMessage("removing door to the NORTH");
            doors.remove(new Door(center.clone().add(0, 0, -2), BlockFace.SOUTH));
            doorsRemoved++;
        } else {
            sender.sendMessage("adding door to the NORTH");
            doors.add(new Door(center.clone().add(0, 0, -1), BlockFace.NORTH));
        }

        if (doorsRemoved < 1)
            noDoorsRemovedLocations.add(center);
    }

    private void buildPlatform(Location center, Material material) {
        synchronized (pairsToPlace) {
            pairsToPlace.add(new LocationMaterialPair(center, material));
        }
    }
}
