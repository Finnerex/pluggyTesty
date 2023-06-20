package co.tantleffbeef.pluggytesty.extra_listeners;


import co.tantleffbeef.mcplanes.BlockManager;
import co.tantleffbeef.mcplanes.event.CustomBlockPlaceEvent;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
