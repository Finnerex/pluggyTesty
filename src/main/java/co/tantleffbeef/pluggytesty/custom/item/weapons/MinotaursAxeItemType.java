package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.*;


public class MinotaursAxeItemType extends SimpleItemType implements InteractableItemType {

    private final Plugin schedulerPlugin;

    private final int COOLDOWN_TICKS = 40;

    // number of axes that have been spawned since last cool down
    private final Map<UUID, Integer> attacks;

    public MinotaursAxeItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.GOLDEN_AXE);
        this.attacks = new HashMap<>();
        this.schedulerPlugin = namespace;
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);
        meta.setLore(Arrays.asList(ChatColor.DARK_GREEN + "Right-Click : Throw up to three axes", ChatColor.DARK_GREEN + "Cooldown : " + COOLDOWN_TICKS / 20f + "s"));
        meta.setUnbreakable(true);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(),"attack_damage", 0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack item, Block block) {
        if(player.hasCooldown(Material.GOLDEN_AXE))
            return true;

        UUID uuid = player.getUniqueId();

        // initialize to zero axes
        attacks.putIfAbsent(uuid, 0);

        // right-clicked, so another attack
        attacks.put(uuid, attacks.get(uuid) + 1);

        Location l = player.getEyeLocation();

        final ItemDisplay axe = player.getWorld().spawn(l, ItemDisplay.class, (display) -> {
            display.setItemStack(new ItemStack(Material.GOLDEN_AXE));
            display.setRotation(l.getYaw() - 90, 0);
        });

        final Vector direction = l.getDirection();

        BukkitRunnable runnable = new BukkitRunnable() {
            int distance = 0;
            final Vector vel = direction.clone().multiply(0.9);

            @Override
            public void run() {
                if (distance > 30) {
                    axe.remove();
                    cancel();
                    return;
                }

                Location location = axe.getLocation();

                // rotate so the axe spins as it moves forward
                final Transformation t = axe.getTransformation();
                t.getRightRotation().rotateLocalZ((float) Math.toRadians(10));
                axe.setTransformation(t);

                Collection<Entity> entities = player.getWorld().getNearbyEntities(location, 0.7, 0.7, 0.7);
                for (Entity e : entities) { // damage all entities in that block space
                    if (e instanceof Damageable damageable && !e.equals(player))
                        damageable.damage(5, player);
                }

                axe.teleport(location.add(vel));

                distance ++;
            }
        };

        runnable.runTaskTimer(schedulerPlugin, 0, 0);

        // 3 axes before cool down
        if (attacks.get(uuid) > 2) {
            player.setCooldown(Material.GOLDEN_AXE, COOLDOWN_TICKS);
            attacks.put(uuid, 0);
        }

        return true;
    }
}
