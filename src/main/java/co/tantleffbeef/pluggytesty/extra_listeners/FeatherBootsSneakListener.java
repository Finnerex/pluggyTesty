package co.tantleffbeef.pluggytesty.extra_listeners;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import co.tantleffbeef.pluggytesty.custom.item.armor.FeatherBootsItemType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FeatherBootsSneakListener implements Listener {

    private final KeyManager<CustomNbtKey> keyManager;
    private final ResourceManager resourceManager;

    public FeatherBootsSneakListener(KeyManager<CustomNbtKey> keyManager, ResourceManager resourceManager) {
        this.keyManager = keyManager;
        this.resourceManager = resourceManager;
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        ItemStack boots = player.getInventory().getBoots();

        if (boots == null)
            return;

        if (CustomItemType.asInstanceOf(FeatherBootsItemType.class, boots, keyManager, resourceManager) == null)
            return;

        if (!event.isSneaking()) {
            player.removePotionEffect(PotionEffectType.SLOW_FALLING);
            return;
        }

        player.addPotionEffect(PotionEffectType.SLOW_FALLING.createEffect(PotionEffect.INFINITE_DURATION, 0));

    }
}
