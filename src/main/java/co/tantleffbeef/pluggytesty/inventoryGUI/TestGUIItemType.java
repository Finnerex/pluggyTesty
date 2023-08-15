package co.tantleffbeef.pluggytesty.inventoryGUI;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestGUIItemType extends SimpleItemType implements InteractableItemType {

    private final InventoryGUI gui;

    public TestGUIItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.ACACIA_LOG);


        gui = new InventoryGUI(18, "TestGUI", Material.BLACK_BED, namespace.getServer());
        gui.addButton(new InventoryButton((event) -> {
            Entity e = event.getWhoClicked();
            e.getWorld().playSound(e, Sound.ENTITY_BAT_AMBIENT, 1, 1);
        }, Material.BAMBOO, "Bat", "play the bat", "ambient sfx"), 3)

        .addButton(new InventoryButton((event) -> {
            Entity e = event.getWhoClicked();
            e.getWorld().createExplosion(e.getLocation(), 20, false, false);
        }, Material.TNT, "Explode!!", "(explode)"), 7);
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {

        gui.displayTo(player);

        return true;
    }
}
