package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class YoyoItemType extends SimpleItemType implements InteractableItemType {

    private final Plugin schedulerPlugin;

    private final Map<UUID, Boolean> myFirstHashMap = new HashMap<>();

    private PlayerInteractEvent event;

    private YoyoItemType yoyo;


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

            int slot = player.getInventory().getHeldItemSlot();

            ItemDisplay fruit = player.getWorld().spawn(player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(5)), ItemDisplay.class, (display) -> {
                display.setItemStack(itemStack);
//                Transformation transforms = display.getTransformation();
//                transforms.getLeftRotation().rotateLocalZ(15);
//                display.setTransformation(transforms);
            });

            LivingEntity smallSlime = player.getWorld().spawn(player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(5)), Slime.class, (slime) -> {
                slime.setSize(0);
                slime.setInvisible(true);
                slime.setInvulnerable(true);
                slime.setLeashHolder(player);
            });


            BukkitRunnable runnable = new BukkitRunnable() {

                int distance = 5;
                @Override
                public void run() {

                    if (myFirstHashMap.get(uuid)) {
                        fruit.remove();
                        smallSlime.remove();
                        this.cancel();
                        return;
                    }
                    else if(!(player.getInventory().getHeldItemSlot() == slot)){
                        fruit.remove();
                        smallSlime.remove();
                        myFirstHashMap.put(uuid, true);
                        this.cancel();
                        return;
                    }

                    if (player.isSneaking())
                        distance++;
                    else if (distance > 5)
                        distance--;


                    fruit.teleport(player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(distance)));

                    Transformation transform = fruit.getTransformation();
                    transform.getLeftRotation().rotateLocalX(15);
                    fruit.setTransformation(transform);

                    smallSlime.teleport(player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(distance)));

                    Collection<Entity> entities = player.getWorld().getNearbyEntities(fruit.getLocation(), 0.7, 0.7, 0.7);
                    for (Entity e : entities) { // damage all entities in that block space
                        if (e instanceof Damageable damageable && !e.equals(player))
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
