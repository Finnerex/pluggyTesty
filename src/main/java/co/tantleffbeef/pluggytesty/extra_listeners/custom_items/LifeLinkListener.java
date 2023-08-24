package co.tantleffbeef.pluggytesty.extra_listeners.custom_items;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import co.tantleffbeef.pluggytesty.custom.item.utility.LifeLinkItemType;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class LifeLinkListener implements Listener {

    // Contains UUIDs of linked players and the player that linked them (shouldn't be static but balls)
    // goofy BiMap so i can use get with the linker
    private static final BiMap<UUID, UUID> playerLinks = HashBiMap.create();
    private final KeyManager<CustomNbtKey> keyManager;
    private final ResourceManager resourceManager;
    private final Server server;

    public LifeLinkListener(KeyManager<CustomNbtKey> keyManager, ResourceManager resourceManager, Server server) {
        this.keyManager = keyManager;
        this.resourceManager = resourceManager;
        this.server = server;
    }

    public static void resetPlayerLink(UUID player) {
        playerLinks.remove(playerLinks.inverse().get(player));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        Player linker = event.getPlayer();

        ItemStack item = linker.getInventory().getItemInMainHand();
        if (CustomItemType.asInstanceOf(LifeLinkItemType.class, item, keyManager, resourceManager) == null)
            return;

        if (!(event.getRightClicked() instanceof Player playerToLink))
            return;

        if (playerLinks.get(playerToLink.getUniqueId()) != null)
            return;

        if (playerLinks.inverse().get(playerToLink.getUniqueId()) != null) {
            linker.sendMessage(ChatColor.RED + "This player is already linking!");
            return;
        }

        playerLinks.put(playerToLink.getUniqueId(), linker.getUniqueId());

        linker.sendMessage(ChatColor.GOLD + "You life linked to " + ChatColor.YELLOW + playerToLink.getDisplayName());
        playerToLink.sendMessage(ChatColor.YELLOW + linker.getDisplayName() + ChatColor.GOLD + " life linked to you");
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player damagedPlayer))
            return;

        UUID linkerUUID = playerLinks.get(damagedPlayer.getUniqueId());

        if (linkerUUID == null)
            return;

        Player linker = server.getPlayer(linkerUUID);

        if (linker == null)
            return;

        if (damagedPlayer.getLocation().distance(linker.getLocation()) > 20)
            return;

        event.setDamage(event.getDamage() / 2);
        linker.damage(event.getDamage() / 2);
    }

}
