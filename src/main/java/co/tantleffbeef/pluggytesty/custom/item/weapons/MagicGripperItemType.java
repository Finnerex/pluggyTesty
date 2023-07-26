package co.tantleffbeef.pluggytesty.custom.item.weapons;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MagicGripperItemType extends SimpleItemType implements InteractableItemType {

    private final Plugin plugin;
    private final Map<UUID, Entity> heldEntities;


    public MagicGripperItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.ENCHANTED_BOOK);
        this.plugin = namespace;
        this.heldEntities = new HashMap<>();
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {

        UUID playerUUID = player.getUniqueId();

        if (heldEntities.get(playerUUID) != null) {
            heldEntities.put(playerUUID, null);
            return false;
        }

        Location playerLocation = player.getEyeLocation();

        RayTraceResult result = player.getWorld().rayTraceEntities(playerLocation.add(playerLocation.getDirection()), playerLocation.getDirection(), 10);

        if (result == null || result.getHitEntity() == null)
            return false;

        heldEntities.put(playerUUID, result.getHitEntity());

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {

                Entity entity = heldEntities.get(playerUUID);

                if (entity == null || entity.isDead()) {
                    heldEntities.put(playerUUID, null);
                    cancel();
                    return;
                }

                Location location = player.getEyeLocation();

                entity.teleport(location.add(location.getDirection().multiply(5)));

            }
        };

        runnable.runTaskTimer(plugin, 0, 2);

        return false;
    }
}
