package co.tantleffbeef.pluggytesty.extra_listeners;


import co.tantleffbeef.mcplanes.BlockManager;
import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.event.CustomBlockPlaceEvent;
import co.tantleffbeef.pluggytesty.custom.item.weapons.TNT.CustomTNT;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class SpecialTntListener implements Listener {

    private final BlockManager blockManager;

    public SpecialTntListener(@NotNull BlockManager blockManager) {
        this.blockManager = blockManager;
    }

    @EventHandler
    public void placeEvent(CustomBlockPlaceEvent event){

        Block tnt = event.getBlock();

        if(!(event.getItemInHand().asBlock() instanceof CustomTNT customTnt))
            return;

        customTnt.explosionEffect(tnt);
    }
}
