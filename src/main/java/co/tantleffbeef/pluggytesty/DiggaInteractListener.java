package co.tantleffbeef.pluggytesty;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;

public class DiggaInteractListener implements Listener {

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        ItemStack blaster = event.getItem();

        if (blaster == null || blaster.getType() != Material.PRISMARINE_SHARD)
            return;

        ItemMeta blasterMeta = blaster.getItemMeta();

        if (blasterMeta == null || blasterMeta.getLore() == null || !(blasterMeta.getLore().get(0).equals(Digga.DIGGA_LORE)))
            return;

        event.setCancelled(true);

        Player player = event.getPlayer();

        Block block = Dig(10000, player.getEyeLocation());

        if (block == null)
            return;

        block.breakNaturally();

    }

        private Block Dig(double range, Location location) {

            World world = location.getWorld();

            RayTraceResult result = world.rayTraceBlocks(location, location.getDirection() , range, FluidCollisionMode.NEVER);

            if (result == null)
                return null;

            return result.getHitBlock();


        }
}

