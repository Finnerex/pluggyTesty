package co.tantleffbeef.pluggytesty;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.Random;

public class SummonInteractListener implements Listener {

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR &&
                event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.ROTTEN_FLESH)
            return;

        ItemMeta meta = item.getItemMeta();

        if (meta == null || meta.getLore() == null || !meta.getLore().get(0).equals(Summon.SUMMON_LORE))
            return;

        Player player = event.getPlayer();

        if (player.hasCooldown(Material.ROTTEN_FLESH))
            return;

        Location playerLocation = player.getEyeLocation();
        float x = (float) playerLocation.getX();
        float y = (float) playerLocation.getY();
        float z = (float) playerLocation.getZ();

        World world = Objects.requireNonNull(playerLocation.getWorld());

        for(int i = 0; i < 3; i ++) {
            float scatterX = x - 3 + new Random().nextFloat() * 6;
            float scatterZ = z - 3 + new Random().nextFloat() * 6;
            Location location = new Location(world, scatterX, y, scatterZ);
            Zombie zombie = (Zombie) world.spawnEntity(location, EntityType.ZOMBIE);
            //zombie.setTarget();
        }

    }
}
