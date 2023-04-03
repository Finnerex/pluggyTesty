package co.tantleffbeef.pluggytesty;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.Random;

public class MagicStickInteractListener implements Listener {
    private static final int FIREBALL_MAXSPEED = 7;
    private static final int FIREBALL_MINSPEED = 4;
    private static final float FIREBALL_STRENGTH = 2.0f;

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR &&
            event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        ItemStack leItem = event.getItem();

        if (leItem == null || leItem.getType() != Material.STICK)
            return;

        ItemMeta leMeta = leItem.getItemMeta();
        if (!leMeta.getLore().get(0).equals(MagicStick.STICK_LORE))
            return;

        Player player = event.getPlayer();

        if (player.hasCooldown(Material.STICK))
            return;

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
        float spreadY = -0.1f + new Random().nextFloat() * 0.2f;
        float spreadX = -0.1f + new Random().nextFloat() * 0.2f;

        fireball.setDirection(playerDirection.rotateAroundY(spreadY).rotateAroundX(spreadX).multiply(speed));

        player.setCooldown(Material.STICK, 20);
    }
}
