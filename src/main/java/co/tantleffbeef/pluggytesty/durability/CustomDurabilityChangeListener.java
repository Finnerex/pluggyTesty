package co.tantleffbeef.pluggytesty.durability;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class CustomDurabilityChangeListener implements Listener {

    private final KeyManager<CustomNbtKey> keyManager;
    private final ResourceManager resourceManager;
    private final CustomDurabilityManager durabilityManager;

    public CustomDurabilityChangeListener(CustomDurabilityManager durabilityManager, KeyManager<CustomNbtKey> keyManager, ResourceManager resourceManager) {
        this.keyManager = keyManager;
        this.resourceManager = resourceManager;
        this.durabilityManager = durabilityManager;
    }

    @EventHandler
    public void onDurabilityLoss(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();

        int ptMax;

        // does not have custom
        CustomDurabilityItemType durabilityItemType = CustomItemType.asInstanceOf(CustomDurabilityItemType.class, item, keyManager, resourceManager);
        Integer changedMatMax = durabilityManager.newMaterialDurabilities.get(item.getType());

        if (durabilityItemType != null)
            ptMax = durabilityItemType.getMaxDurability();
        else if (changedMatMax != null)
            ptMax = changedMatMax;
        else
            return;

        // item has not been damaged before
        durabilityManager.durabilities.putIfAbsent(item, ptMax);
        int newDurability = durabilityManager.durabilities.get(item) - event.getDamage();

        // I have to remove it because it's a different itemstack when the meta changes
        durabilityManager.durabilities.remove(item);

        if (newDurability <= 0) {
            event.setDamage(999999); // a big number
            return;
        }

        Bukkit.broadcastMessage("new PT dur: " + newDurability);
        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "durabilities" + durabilityManager.durabilities);

        Damageable damageMeta = (Damageable) item.getItemMeta();
        assert damageMeta != null;

        Bukkit.broadcastMessage(ChatColor.GREEN + "Old game dur: " + damageMeta.getDamage());

        // change durability bar in game
        int gameMax = item.getType().getMaxDurability();
        damageMeta.setDamage(gameMax - (newDurability * item.getType().getMaxDurability() / ptMax));

        Bukkit.broadcastMessage(ChatColor.AQUA + "New game dur: " + damageMeta.getDamage());

        item.setItemMeta(damageMeta);

        event.setDamage(0);

        // internally change durability
        durabilityManager.durabilities.put(item, newDurability);

    }

}
