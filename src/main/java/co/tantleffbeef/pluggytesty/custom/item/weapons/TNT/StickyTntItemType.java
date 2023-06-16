package co.tantleffbeef.pluggytesty.custom.item.weapons.TNT;

import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import co.tantleffbeef.mcplanes.custom.item.SimplePlaceableItemType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.bukkit.Bukkit;

import java.util.List;


public class StickyTntItemType extends SimplePlaceableItemType implements CustomTNT {

    private final Plugin plugin;



    public StickyTntItemType(Plugin namespace, String id, boolean customModel, String name){
        super(namespace, id, customModel, name);
        this.plugin = namespace;
    }

    @Override
    public @NotNull Material baseMaterial() {
        return Material.TNT;
    }
    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);
        meta.setLore(List.of(ChatColor.DARK_GREEN + "SUPER Sticky!!!"));
    }

    @Override
    public void explosionEffect(Block tnt){
        FallingBlock block = tnt.getWorld().spawnFallingBlock(tnt.getLocation(), Material.LAVA, (byte) 0);
        float x = (float) -1 + (float) (Math.random() * ((1 - -1) + 1));
        float y = (float) -5 + (float)(Math.random() * ((5 - -5) + 1));
        float z = (float) -0.3 + (float)(Math.random() * ((0.3 - -0.3) + 1));
        Bukkit.broadcastMessage("§c" + x + ", §a" + y + ", §d" + z);
        block.setVelocity(new Vector(x, y, z));
    }
}










