package co.tantleffbeef.pluggytesty.custom.item.weapons.TNT;

import co.tantleffbeef.mcplanes.custom.block.InteractableBlockType;
import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import co.tantleffbeef.mcplanes.custom.item.SimplePlaceableItemType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;
import org.bukkit.util .Vector;

import java.util.List;


public class StickyTntItemType extends SimplePlaceableItemType implements InteractableBlockType {

    private final Plugin plugin;
    private final Vector displacementVec = new Vector(0, 5, 0);



    public StickyTntItemType(Plugin namespace, String id, boolean customModel, String name){
        super(namespace, id, customModel, name);
        this.plugin = namespace;
    }
    @Override
    public @NotNull Material baseMaterial() {
        return Material.BARRIER;
    }
    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);
        meta.setLore(List.of(ChatColor.DARK_GREEN + "SUPER Sticky!!!"));
    }

    @Override
    public boolean interactBlock(@NotNull Player player, @NotNull Location location, @NotNull Block block, @NotNull Action action) {
        FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(block.getLocation().add(displacementVec), Material.GRASS_BLOCK.createBlockData());
        float x = (float) -1 + (float) (Math.random() * ((1 - -1) + 1));
        float y = (float) -5 + (float) (Math.random() * ((5 - -5) + 1));
        float z = (float) -0.3 + (float) (Math.random() * ((0.3 - -0.3) + 1));
        Bukkit.broadcastMessage("§c" + x + ", §a" + y + ", §d" + z);
        fallingBlock.setVelocity(new Vector(x, y, z));
        return false;
    }
}










