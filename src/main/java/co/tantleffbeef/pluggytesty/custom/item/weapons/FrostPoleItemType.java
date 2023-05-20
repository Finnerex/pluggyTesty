package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.CustomItemType;
import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Random;

public class FrostPoleItemType extends SimpleItemType implements InteractableItemType {

    private final int COOLDOWN_TICKS = 5;

    public FrostPoleItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.SOUL_TORCH);
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addEnchant(Enchantment.FROST_WALKER, 2, true);

        meta.setLore(Arrays.asList(ChatColor.DARK_GREEN + "Right-Click : Freezing bolt", ChatColor.DARK_GREEN + "Cooldown : " + COOLDOWN_TICKS / 20f));
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack item, Block block) {
        if (player.hasCooldown(Material.SOUL_TORCH)) return true;

        player.setCooldown(Material.SOUL_TORCH, COOLDOWN_TICKS);
        player.getWorld().playSound(player, Sound.ENTITY_PLAYER_HURT_FREEZE, 1, 1);

        final float range = 2f;
        Entity target = shootBolt(range, player);

        if (target != null)
            target.setFreezeTicks(260);

        return true;
    }

    private Entity shootBolt(float r, Player p) {
        Location location = p.getEyeLocation();
        location.add(location.getDirection().multiply(2));
        final World world = location.getWorld();

        Entity entity = null;
        RayTraceResult result = p.getWorld().rayTraceEntities(location, location.getDirection(), r * 10);
        if (result != null)
            entity = result.getHitEntity();


        for(float i = 0.1f; i < r; i += 0.1f) {
            location.add(location.getDirection().multiply(i));
            Random x = new Random();
            Location location2 = location.clone();
            world.spawnParticle(Particle.DOLPHIN, location, 1);
            double spread = 0.25;
            world.spawnParticle(Particle.DOLPHIN, location2.add((x.nextDouble()+0.5)*spread, (x.nextDouble()+0.5)*spread, (x.nextDouble()+0.5)*spread), 1);
        }

        return entity;
    }


}
