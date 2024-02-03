package co.tantleffbeef.pluggytesty.custom.item.utility;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HeartTransplantItemType extends SimpleItemType implements InteractableItemType {

    private final Map<UUID, Integer> eatenNumbers; // stores which players have eaten how many hearts

    public HeartTransplantItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.RED_DYE);
        eatenNumbers = new HashMap<>();
        namespace.getServer().getPluginManager().registerEvents(new HeartTransplantServerStateListener(), namespace);
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack item, @Nullable Block targetBlock) {

        UUID uuid = player.getUniqueId();

        int numEaten = eatenNumbers.get(uuid);

        if (numEaten < 10) {
            eatenNumbers.put(uuid, numEaten + 1);

            AttributeInstance maxHealthAttr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            assert maxHealthAttr != null;

            maxHealthAttr.setBaseValue(maxHealthAttr.getValue() + 1);
        }

        item.setAmount(item.getAmount() - 1);

        return true;
    }

    private class HeartTransplantServerStateListener implements Listener {

        // probably should do this with server starting and closing but there aren't really events for this
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            eatenNumbers.putIfAbsent(event.getPlayer().getUniqueId(), 0);

            // load that shit
        }

        @EventHandler
        public void onPlayerLeave(PlayerQuitEvent event) {
            // save that shit
        }

    }
}
