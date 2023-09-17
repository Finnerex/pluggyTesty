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

import java.util.HashMap;
import java.util.Map;

public class CustomDurabilityManager implements Listener {

    private final Map<ItemStack, Integer> durabilities; // probably wont work??
    private final KeyManager<CustomNbtKey> keyManager;
    private final ResourceManager resourceManager;

    public CustomDurabilityManager(KeyManager<CustomNbtKey> keyManager, ResourceManager resourceManager) {
        this.durabilities = new HashMap<>();
        this.keyManager = keyManager;
        this.resourceManager = resourceManager;
    }

    @EventHandler
    public void onDurabilityLoss(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();

        // does not have custom
        CustomDurabilityItemType durabilityItemType = CustomItemType.asInstanceOf(CustomDurabilityItemType.class, item, keyManager, resourceManager);
        if (durabilityItemType == null)
            return;

        // PluggyTesty maximum durability for this item
        int ptMax = durabilityItemType.getMaxDurability();

        // item has not been damaged before
        durabilities.putIfAbsent(item, ptMax);
        int newDurability = durabilities.get(item) - event.getDamage();

        // I have to remove it because it's a different itemstack when the meta changes
        durabilities.remove(item);

//        if (newDurability <= 0) {
//            event.setDamage(999999); // a big number
//            return;
//        }

        Bukkit.broadcastMessage("new PT dur: " + newDurability);
        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "durabilities" + durabilities);

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
        durabilities.put(item, newDurability);

    }


    private void saveDurabilities() {

    }

    private void loadDurabilities() {

    }

}
