package co.tantleffbeef.pluggytesty.armor;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class LeatherT2 extends SimpleItemType implements InteractableItemType {
    private static final int ARMOR = 7;
    private static final int ARMOR_TOUGHNESS = 4;

    public MagicStickItemType(@NotNull Plugin namespace, @NotNull String id, boolean customModel, @NotNull String name) {
        super(namespace, id, customModel, name);
    }
