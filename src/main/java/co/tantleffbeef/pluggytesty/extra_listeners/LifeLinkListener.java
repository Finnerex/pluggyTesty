package co.tantleffbeef.pluggytesty.extra_listeners;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import co.tantleffbeef.pluggytesty.custom.item.utility.LifeLinkItemType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LifeLinkListener implements Listener {

    // Contains UUIDs of linked players and the player that linked to them
    private final Map<UUID, UUID> playerLinks;
    private final KeyManager<CustomNbtKey> keyManager;
    private final ResourceManager resourceManager;
    private final Server server;

    public LifeLinkListener(KeyManager<CustomNbtKey> keyManager, ResourceManager resourceManager, Server server) {
        this.keyManager = keyManager;
        this.resourceManager = resourceManager;
        this.server = server;
        this.playerLinks = new HashMap<>();
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

        playerLinks.put(playerToLink.getUniqueId(), linker.getUniqueId());

        linker.sendMessage(ChatColor.GOLD + "You life linked to " + ChatColor.YELLOW + playerToLink.getDisplayName());
        playerToLink.sendMessage(ChatColor.YELLOW + linker.getDisplayName() + ChatColor.GOLD + " life linked to you");
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player damagedPlayer))
            return;

        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "player damaged");

        UUID linkerUUID = playerLinks.get(damagedPlayer.getUniqueId());

        if (linkerUUID == null)
            return;

        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "linker id exists");

        Player linker = server.getPlayer(linkerUUID);

        if (linker == null)
            return;

        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "linker exists");

        if (damagedPlayer.getLocation().distance(linker.getLocation()) > 20)
            return;

        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "distance <= 20");

        event.setDamage(event.getDamage() / 2);
        linker.damage(event.getDamage() / 2);
        linker.setLastDamageCause(event);
    }

}
