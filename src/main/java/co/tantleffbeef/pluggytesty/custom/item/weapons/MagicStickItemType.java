package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Random;

public final class MagicStickItemType extends SimpleItemType implements InteractableItemType {
    private static final int FIREBALL_MAXSPEED = 7;
    private static final int FIREBALL_MINSPEED = 4;
    private static final float FIREBALL_STRENGTH = 2.0f;

    public MagicStickItemType(@NotNull Plugin namespace, @NotNull String id, boolean customModel, @NotNull String name) {
        super(namespace, id, customModel, name);
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {
        if (player.hasCooldown(Material.STICK))
            return false;

        Location playerLocation = player.getEyeLocation();
        Vector playerDirection = playerLocation.getDirection().normalize();
        // get player direction

        // summon a fireball in correct direction
        World fireballWorld = Objects.requireNonNull(playerLocation.getWorld());
        Fireball fireball = (Fireball) fireballWorld.spawnEntity(
                playerLocation.add(playerDirection), EntityType.FIREBALL);

        //change fireballyness
        fireball.setYield(FIREBALL_STRENGTH);
        fireball.setIsIncendiary(true);

        float speed = FIREBALL_MINSPEED + new Random().nextFloat() * (FIREBALL_MAXSPEED - FIREBALL_MINSPEED);
        float spreadY = -0.1f + new Random().nextFloat() * 0.2f;
        float spreadX = -0.1f + new Random().nextFloat() * 0.2f;

        fireball.setDirection(playerDirection.rotateAroundY(spreadY).rotateAroundX(spreadX).multiply(speed));

        player.setCooldown(Material.STICK, 20);

        return false;
    }
}
