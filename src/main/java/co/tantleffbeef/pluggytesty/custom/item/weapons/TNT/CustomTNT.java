package co.tantleffbeef.pluggytesty.custom.item.weapons.TNT;

import co.tantleffbeef.mcplanes.custom.block.CustomBlockType;
import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;

public interface CustomTNT extends CustomBlockType {

/**
 * Runs when the custom TNT explodes
 * Changes what happens after the TNT explodes
 */
    void explosionEffect(Block tnt);
}
