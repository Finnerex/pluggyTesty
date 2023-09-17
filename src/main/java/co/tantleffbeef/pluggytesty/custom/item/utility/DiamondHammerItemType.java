package co.tantleffbeef.pluggytesty.custom.item.utility;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class DiamondHammerItemType extends SimpleItemType {

    private final KeyManager<CustomNbtKey> keyManager;
    private final ResourceManager resourceManager;

    public DiamondHammerItemType(Plugin namespace, String id, boolean customModel, String name, KeyManager<CustomNbtKey> keyManager, ResourceManager resourceManager) {
        super(namespace, id, customModel, name, Material.DIAMOND_PICKAXE);

        namespace.getServer().getPluginManager().registerEvents(new DiamondHammerBreakListener(), namespace);
        this.resourceManager = resourceManager;
        this.keyManager = keyManager;
    }

    private class DiamondHammerBreakListener implements Listener {
        @EventHandler
        public void onBlockBreak(BlockBreakEvent event) {

            DiamondHammerItemType hammer = CustomItemType.asInstanceOf(DiamondHammerItemType.class, event.getPlayer().getInventory().getItemInMainHand(), keyManager, resourceManager);

            if (hammer == null)
                return;

            BlockFace face = event.getPlayer().getFacing();

            Vector right = switch (face) {
                case NORTH -> BlockFace.WEST.getDirection();
                case SOUTH -> BlockFace.EAST.getDirection();
                case EAST -> BlockFace.NORTH.getDirection();
                case WEST -> BlockFace.SOUTH.getDirection();
                default -> BlockFace.WEST.getDirection(); // idk man
            };

            Location currentBlockLoc = event.getBlock().getLocation();

            // current block is now bottom left
            currentBlockLoc.subtract(right);
            currentBlockLoc.subtract(0, 1, 0);

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {

                    if (i == 1 && j == 1) continue;

                    currentBlockLoc.getBlock().breakNaturally();
                    currentBlockLoc.add(right);
                }
                currentBlockLoc.subtract(right.clone().multiply(3));
                currentBlockLoc.add(0, 1, 0);

                Bukkit.broadcastMessage("block: " + currentBlockLoc.toVector());
            }
        }
    }
}
