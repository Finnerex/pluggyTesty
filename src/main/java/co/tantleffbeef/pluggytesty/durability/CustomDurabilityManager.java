package co.tantleffbeef.pluggytesty.durability;

import co.tantleffbeef.mcplanes.CustomNbtKey;
import co.tantleffbeef.mcplanes.KeyManager;
import co.tantleffbeef.mcplanes.ResourceManager;
import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.HashMap;
import java.util.Map;

public class CustomDurabilityManager implements Listener {

//    public Map<ItemStack, Integer> durabilities; // probably wont work??
    private final Map<Material, Integer> newMaterialDurabilities;


    public CustomDurabilityManager() {
//        this.durabilities = new HashMap<>();
        this.newMaterialDurabilities = new HashMap<>();
    }

    public void registerNewDurability(Material material, int durability) {
        newMaterialDurabilities.put(material, durability);
    }

    public Integer getMaterialDurability(Material material) {
        return newMaterialDurabilities.get(material);
    }

//    private void saveDurabilities() {
//
//    }
//
//    private void loadDurabilities() {
//
//    }

}
