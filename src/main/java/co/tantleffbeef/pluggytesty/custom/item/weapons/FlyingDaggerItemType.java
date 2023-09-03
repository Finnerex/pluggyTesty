package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FlyingDaggerItemType extends SimpleItemType implements InteractableItemType {
    // I am making this simply because I really wanted to use a queue in something.

    private final Map<UUID, Queue<Entity>> entityQueues;
    private final Map<UUID, Boolean> toggles;
    private final Plugin plugin;

    public FlyingDaggerItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.SPECTRAL_ARROW);
        entityQueues = new HashMap<>();
        toggles = new HashMap<>();
        plugin = namespace;
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack item, @Nullable Block targetBlock) {
        UUID playerUUID = player.getUniqueId();

        entityQueues.putIfAbsent(playerUUID, new ArrayDeque<>());

        if (player.isSneaking()) {
            if (toggles.get(playerUUID))
                return false;

            // get looking at entity, add to the queue
            Location l = player.getEyeLocation();
            RayTraceResult result = player.getWorld().rayTraceEntities(l, l.getDirection(), 20);

            if (result == null || result.getHitEntity() == null)
                return false;

            entityQueues.get(playerUUID).offer(result.getHitEntity());
            Bukkit.broadcastMessage("queue: " + entityQueues.get(playerUUID));

        } else {
            toggles.putIfAbsent(playerUUID, false);
            toggles.put(playerUUID, !toggles.get(playerUUID));

            if (toggles.get(playerUUID)) {
                // summon arrow, start going

                Arrow arrow = player.getWorld().spawnArrow(player.getEyeLocation().add(0, 0, 1), new Vector(1, 0, 0), 0.6f, 0);
                arrow.setPierceLevel(255);

                BukkitRunnable runnable = new BukkitRunnable() {
                    Entity attacking = null;
                    int lastPeirceLevel = arrow.getPierceLevel() + 1;
                    @Override
                    public void run() {
                        Queue<Entity> entityQueue = entityQueues.get(playerUUID);

                        if (!toggles.get(playerUUID) || arrow.isInBlock() || arrow.isDead()) {
                            arrow.remove();
                            entityQueues.get(playerUUID).clear();
                            cancel();
                            return;
                        }

                        if (attacking == null || attacking.isDead())
                            attacking = entityQueue.poll();

                        if (attacking == null || attacking.isDead()) {
                            // orbit the player
                            arrow.setVelocity(arrow.getVelocity().add(arrow.getLocation().clone().subtract(player.getLocation()).toVector()));

                        } else {
                            arrow.setVelocity(attacking.getLocation().subtract(arrow.getLocation()).toVector().normalize());

                            // attack the next entity
                            if (arrow.getPierceLevel() < lastPeirceLevel)
                                attacking = entityQueue.poll();

                            if (attacking == null || attacking.isDead()) {
                                arrow.teleport(player.getEyeLocation().add(0, 0, 1));
                                arrow.setVelocity(new Vector(1, 0, 0));
                            }

                            arrow.setVelocity(attacking.getLocation().clone().subtract(arrow.getLocation()).toVector().normalize());

                            lastPeirceLevel = arrow.getPierceLevel();
                            Bukkit.broadcastMessage("pierce: " + arrow.getPierceLevel());

                        }

                    }
                };

                runnable.runTaskTimer(plugin, 0, 0);

            }
        }

        return false;
    }

}
