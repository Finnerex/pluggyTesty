package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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

    private final Map<UUID, Queue<LivingEntity>> entityQueues;
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

            // get looking at entity, add to the queue
            Location l = player.getEyeLocation();
            RayTraceResult result = player.getWorld().rayTraceEntities(l.add(l.getDirection()), l.getDirection(), 20);

            if (result == null || !(result.getHitEntity() instanceof LivingEntity))
                return false;

            entityQueues.get(playerUUID).offer((LivingEntity) result.getHitEntity());
            Bukkit.broadcastMessage("queue: " + entityQueues.get(playerUUID));

        } else {
            toggles.putIfAbsent(playerUUID, false);
            toggles.put(playerUUID, !toggles.get(playerUUID));

            if (toggles.get(playerUUID)) {
                // summon arrow, start going

                Arrow arrow = player.getWorld().spawnArrow(player.getEyeLocation(), new Vector(0, 0, 0), 0.6f, 0);
                arrow.setPierceLevel(127);
                arrow.setGravity(false);

                BukkitRunnable runnable = new BukkitRunnable() {
                    LivingEntity attacking = null;
                    int lastPeirceLevel = arrow.getPierceLevel();
                    @Override
                    public void run() {
                        Queue<LivingEntity> entityQueue = entityQueues.get(playerUUID);

                        if (!toggles.get(playerUUID) || arrow.isInBlock() || arrow.isDead() || arrow.isOnGround()) {
                            arrow.remove();
                            entityQueues.get(playerUUID).clear();
                            cancel();
                            return;
                        }

                        if (attacking == null || attacking.isDead())
                            attacking = entityQueue.poll();

                        if (attacking == null || attacking.isDead()) {
                            // orbit the player
                            arrow.teleport(player.getEyeLocation().add(player.getEyeLocation().getDirection().setY(0).rotateAroundY(90)));

                        } else {
                            arrow.setVelocity(attacking.getEyeLocation().clone().subtract(arrow.getLocation()).toVector().normalize());
                            // attack the next entity
                            if (arrow.getPierceLevel() < lastPeirceLevel)
                                attacking = entityQueue.poll();

//                            if (attacking == null || attacking.isDead()) {
//                                arrow.teleport(player.getEyeLocation().add(0, 0, 1));
//                                arrow.setVelocity(new Vector(1, 0, 0));
//                            }

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
