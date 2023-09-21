package co.tantleffbeef.pluggytesty.durability;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;


public class CustomDurabilityChangeListener implements Listener {

    private final KeyManager<CustomNbtKey> keyManager;
    private final ResourceManager resourceManager;
    private final CustomDurabilityManager durabilityManager;
    private final NamespacedKey DURABILITY_KEY;

    public CustomDurabilityChangeListener(Plugin plugin, CustomDurabilityManager durabilityManager, KeyManager<CustomNbtKey> keyManager, ResourceManager resourceManager) {
        this.keyManager = keyManager;
        this.resourceManager = resourceManager;
        this.durabilityManager = durabilityManager;

        DURABILITY_KEY = new NamespacedKey(plugin, "custom-durability");
    }

    @EventHandler
    public void onDurabilityLoss(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();

        int ptMax;

        CustomDurabilityItemType durabilityItemType = CustomItemType.asInstanceOf(CustomDurabilityItemType.class, item, keyManager, resourceManager);
        Integer changedMatMax = durabilityManager.getMaterialDurability(item.getType());

        // custom item with changed durability
        if (durabilityItemType != null)
            ptMax = durabilityItemType.getMaxDurability();
        // material with changed durability
        else if (changedMatMax != null)
            ptMax = changedMatMax;
        // unchanged durability
        else
            return;

        Damageable damageMeta = (Damageable) item.getItemMeta();
        assert damageMeta != null;

        PersistentDataContainer itemDataContainer = damageMeta.getPersistentDataContainer();

        // item has not been damaged before
        if (!itemDataContainer.has(DURABILITY_KEY, PersistentDataType.INTEGER))
            itemDataContainer.set(DURABILITY_KEY, PersistentDataType.INTEGER, ptMax);


        int newDurability = itemDataContainer.get(DURABILITY_KEY, PersistentDataType.INTEGER) - event.getDamage();

        // break item if no more durability
        if (newDurability <= 0) {
            event.setDamage(999999); // a big number
            return;
        }

        // change durability bar in game
        int gameMax = item.getType().getMaxDurability();
        damageMeta.setDamage(gameMax - (newDurability * item.getType().getMaxDurability() / ptMax));

        event.setDamage(0);

        // internally change durability
        itemDataContainer.set(DURABILITY_KEY, PersistentDataType.INTEGER, newDurability);

        item.setItemMeta(damageMeta);


    }

}
