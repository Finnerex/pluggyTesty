package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class YoyoItemType extends SimpleItemType implements InteractableItemType {

    private final Plugin schedulerPlugin;

    private final Map<UUID, Boolean> myFirstHashMap = new HashMap<>();

    public YoyoItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.CHORUS_FRUIT);
        this.schedulerPlugin = namespace;
    }


    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {
        if (player.hasCooldown(Material.CHORUS_FRUIT))
            return true;


        UUID uuid = player.getUniqueId();
        myFirstHashMap.putIfAbsent(uuid, true);

        if (myFirstHashMap.get(uuid)) {

            myFirstHashMap.put(uuid, false);

            ItemDisplay fruit = player.getWorld().spawn(player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(5)), ItemDisplay.class, (display) -> {
                display.setItemStack(itemStack);
                display.setRotation(player.getEyeLocation().getYaw() - 90, player.getEyeLocation().getPitch() - 90);
            });

            LivingEntity smallSlime = player.getWorld().spawn(player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(5)), Slime.class, (slime) -> {
//                slime.setSize(1);
//                slime.setInvisible(true);
                slime.setInvulnerable(true);
//                slime.setLeashHolder(player);
            });


            BukkitRunnable runnable = new BukkitRunnable() {

                int distance = 5;
                final PlayerInventory inventory = player.getInventory();
                @Override
                public void run() {

                    if(!inventory.getItemInMainHand().equals(itemStack))
                        myFirstHashMap.put(uuid, true);
                    
                    if (myFirstHashMap.get(uuid)) {
                        fruit.remove();
                        smallSlime.remove();
                        this.cancel();
                        return;
                    }

                    if (player.isSneaking())
                        distance++;
                    else if (distance > 5)
                        distance--;


                    fruit.teleport(player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(distance)));
                    fruit.setRotation(0, player.getEyeLocation().getPitch() - 90);

                    Transformation transform = fruit.getTransformation();
                    transform.getLeftRotation().rotateLocalZ(10);
                    fruit.setTransformation(transform);

//                    smallSlime.teleport(player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(distance)));

                    Collection<Entity> entities = player.getWorld().getNearbyEntities(fruit.getLocation(), 0.7, 0.7, 0.7);
                    for (Entity e : entities) { // damage all entities in that block space
                        if (e instanceof Damageable damageable && !e.equals(player) /*&& !e.equals(smallSlime)*/)
                            damageable.damage(5, player);
                    }

                }
            };

            runnable.runTaskTimer(schedulerPlugin, 0, 0);

        } else
            myFirstHashMap.put(uuid, true);


        return true;
    }

}
