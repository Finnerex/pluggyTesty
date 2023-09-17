package co.tantleffbeef.pluggytesty.durability;

import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

public class TestDurabilityItemType extends SimpleItemType implements CustomDurabilityItemType {
    public TestDurabilityItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.DIAMOND_PICKAXE);
    }

    @Override
    public int getMaxDurability() {
        return 10;
    }

}
