package co.tantleffbeef.pluggytesty.custom.item.utility;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Location;
import org.bukkit.Material;
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

            Location currentBlockLoc = event.getBlock().getLocation();
            Vector right = event.getPlayer().getEyeLocation().getDirection().setY(0).rotateAroundY(90);

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
            }
        }
    }
}
