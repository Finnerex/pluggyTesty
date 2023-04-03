package co.tantleffbeef.pluggytesty;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.Random;

public class PlayerInteractEventListener implements Listener {
    private static final int FIREBALL_MAXSPEED = 7;
    private static final int FIREBALL_MINSPEED = 4;
    private static final float FIREBALL_STRENGTH = 2.0f;

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack leItem = event.getItem();

        if (leItem == null || leItem.getType() != Material.STICK)
            return;

        ItemMeta leMeta = leItem.getItemMeta();
        if (!leMeta.getLore().get(0).equals(MagicStick.STICK_LORE))
            return;

        Player player = event.getPlayer();

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
        float spreadY = -0.5f + new Random().nextFloat();
        float spreadX = -0.5f + new Random().nextFloat();

        fireball.setDirection(playerDirection.rotateAroundY(spreadY).rotateAroundX(spreadX).multiply(speed));

        player.setCooldown(Material.STICK, 20);
    }
}
