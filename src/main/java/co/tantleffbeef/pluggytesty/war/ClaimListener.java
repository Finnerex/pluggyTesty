package co.tantleffbeef.pluggytesty.war;

import co.tantleffbeef.pluggytesty.goober.GooberStateController;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class ClaimListener implements Listener {
    private static final BaseComponent BUILD_IN_OTHER_CLAIM_MSG = new ComponentBuilder("You cannot build in this claim! It is owned by a different team.").color(ChatColor.RED).build();
    private final ClaimWorldController controller;
    private final GooberStateController gooberController;

    public ClaimListener(ClaimWorldController controller, GooberStateController gooberController) {
        this.controller = controller;
        this.gooberController = gooberController;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        var location = event.getBlock().getLocation();
        var world = controller.getClaimWorld(location.getWorld());
        var chunk = BlockPosition.chunkPositionOf(location);

        if (!world.isChunkClaimed(chunk))
            return;

        var claimTeam = world.getChunkClaim(chunk);
        var goober = gooberController.wrapPlayer(event.getPlayer());

        if (claimTeam.isGooberMember(goober))
            return;

        event.setCancelled(true);
        goober.asPlayer().spigot().sendMessage(BUILD_IN_OTHER_CLAIM_MSG);
    }
}
