package co.tantleffbeef.pluggytesty.custom.item.utility;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class SwiftStaffItemType extends SimpleItemType implements InteractableItemType {

    public SwiftStaffItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.STICK);
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack item, Block block) {
        if (player.hasCooldown(Material.STICK)) {
            return false;
        }

        player.getWorld().playSound(player, Sound.ENTITY_SPLASH_POTION_BREAK, 1, 1);

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 4));

        player.setCooldown(Material.STICK, 600);

        return false;
    }

}
