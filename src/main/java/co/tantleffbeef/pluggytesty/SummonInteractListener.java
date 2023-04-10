package co.tantleffbeef.pluggytesty;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class SummonInteractListener implements Listener {

    private final Plugin plugin;
    private static HashMap<Player, ArrayList<Zombie>> summonOwners = new HashMap<>();

    public SummonInteractListener(Plugin plugin) {
        this.plugin = plugin;
    }
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

        event.setCancelled(true);

        Player player = event.getPlayer();

        if (player.hasCooldown(Material.ROTTEN_FLESH))
            return;

        Location playerLocation = player.getEyeLocation();
        float x = (float) playerLocation.getX();
        float y = (float) playerLocation.getY();
        float z = (float) playerLocation.getZ();

        World world = Objects.requireNonNull(playerLocation.getWorld());

        ArrayList<Zombie> summons = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            float scatterX = x - 3 + new Random().nextFloat() * 6;
            float scatterZ = z - 3 + new Random().nextFloat() * 6;
            Location location = new Location(world, scatterX, y, scatterZ);
            summons.add((Zombie) world.spawnEntity(location, EntityType.ZOMBIE));
        }

        // schedule the targeting for later because they always target player first

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                LivingEntity target = getNearestEntity(playerLocation, player);
                for (Zombie s : summons) {
                    if (target != null) {
                        s.setTarget(target);
                        player.sendMessage("Target: " + target);
                    }
                }
                cancel();
            }
        };

        runnable.runTaskTimer(plugin, 20, 0);

        if (summonOwners.containsKey(player))
            summonOwners.get(player).addAll(summons);
        else
            summonOwners.put(player, summons);

        player.sendMessage(summonOwners.toString());

    }

    private LivingEntity getNearestEntity(Location l, Player player) {

        ArrayList<Entity> entities = (ArrayList<Entity>) player.getNearbyEntities(50, 10, 50);
        if (entities.get(0) == null)
           return null;

        LivingEntity closest = null;
        double closestDist = -1;

        for (Entity e : entities) {
            double d = e.getLocation().distance(l);
            if (!(e instanceof Zombie) && !e.equals(player) && e instanceof LivingEntity && (closestDist == -1 || d < closestDist)) {
                closestDist = d;
                closest = (LivingEntity) e;
            }
        }

        return closest;
    }

    @EventHandler
    private void onTargetChanged(EntityTargetEvent event) {
        if (!(event.getEntity() instanceof Zombie))
            return;

        Zombie zombie = (Zombie) event.getEntity();
        Entity target = event.getTarget();

        if (!(target instanceof Player))
            return;

        Player player = (Player) target;
        ArrayList<Zombie> summons = summonOwners.get(player);

        player.sendMessage(summonOwners.toString());

        if (summons.contains(zombie)) // if the player owns the zombie
            zombie.setTarget(getNearestEntity(player.getEyeLocation(), player));

    }

    @EventHandler
    private void onDeath(EntityDeathEvent event) {

    }


}
