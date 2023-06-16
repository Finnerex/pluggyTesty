package co.tantleffbeef.pluggytesty.extra_listeners;


import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.pluggytesty.custom.item.weapons.TNT.CustomTNT;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class SpecialTntListener implements Listener {

    private final KeyManager<CustomNbtKey> keyManager;
    private final ResourceManager resourceManager;
    private final Plugin plugin;

    public SpecialTntListener(@NotNull KeyManager<CustomNbtKey> keyManager, @NotNull ResourceManager resourceManager, @NotNull Plugin plugin) {
        this.keyManager = keyManager;
        this.resourceManager = resourceManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void placeEvent(BlockPlaceEvent event){

        Block tnt = event.getBlock();

        if(!(event.getBlock() instanceof CustomTNT customTnt))
            return;

        customTnt.explosionEffect(tnt);
    }
}
