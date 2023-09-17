package co.tantleffbeef.pluggytesty.durability;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
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

        // internally change durability
        durabilities.put(item, durabilities.get(item) - event.getDamage());


        Damageable damageMeta = (Damageable) item.getItemMeta();
        assert damageMeta != null;

        // change durability bar in game
        damageMeta.setDamage((durabilities.get(item) * item.getType().getMaxDurability()) / ptMax);

        item.setItemMeta(damageMeta);

        event.setDamage(0);

    }


    private void saveDurabilities() {

    }

    private void loadDurabilities() {

    }

}
