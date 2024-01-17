package co.tantleffbeef.pluggytesty.custom.item.utility;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
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
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack item, @Nullable Block targetBlock) {

        UUID uuid = player.getUniqueId();

        eatenNumbers.putIfAbsent(uuid, 0);

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
}
