package co.tantleffbeef.pluggytesty.custom.item.utility;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import co.tantleffbeef.pluggytesty.expeditions.parties.Party;
import co.tantleffbeef.pluggytesty.goober.GooberStateController;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class WildWarpItemType extends SimpleItemType implements InteractableItemType {

    private final int TP_TIME_MINUTES = 60;
    private final int TIMER_PERIOD_MINUTES = 10;
    private final int TP_DIST_MIN = 50_000;
    private final int TP_DIST_MAX = 80_000;
    private class WildWarpData {
        public int timeLeftMins;
        public final Location from;
        public final Location to;
        WildWarpData(Location from, Location to, int timeLeftMins) {
            this.from = from;
            this.to = to;
            this.timeLeftMins = timeLeftMins;
        }
    }
    private final Map<UUID, WildWarpData> warpedPlayers;
    private GooberStateController gooberStateController;
    private Plugin plugin;

    public WildWarpItemType(Plugin namespace, String id, boolean customModel, String name, GooberStateController gooberStateController) {
        super(namespace, id, customModel, name, Material.AMETHYST_SHARD);
        warpedPlayers = new HashMap<>();
        this.gooberStateController = gooberStateController;
        plugin = namespace;
    }


    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack item, @Nullable Block targetBlock) {

        Optional<Party> party = gooberStateController.wrapPlayer(player).getParty();

        Random rand = new Random();

        Location to = new Location(player.getWorld(), rand.nextInt(TP_DIST_MIN, TP_DIST_MAX) * (rand.nextBoolean() ? -1 : 1),
                                   rand.nextInt(TP_DIST_MIN, TP_DIST_MAX) * (rand.nextBoolean() ? -1 : 1), 0);
        to.setY(player.getWorld().getHighestBlockAt(to).getY());


        if (party.isPresent()) {
            for (Player p : party.get().getOnlinePlayers()) {
                warpedPlayers.put(p.getUniqueId(), new WildWarpData(p.getLocation(), to, TP_TIME_MINUTES));
                p.teleport(to);
            }
        } else {
            warpedPlayers.put(player.getUniqueId(), new WildWarpData(player.getLocation(), to, TP_TIME_MINUTES));
            player.teleport(to);
        }


        BukkitRunnable tpTimer = new BukkitRunnable() {
            @Override
            public void run() {
                if (party.isPresent()) {
                    for (Player p : party.get().getOnlinePlayers()) {
                        WildWarpData data = warpedPlayers.get(p.getUniqueId());

                        data.timeLeftMins -= TIMER_PERIOD_MINUTES;
                        p.sendMessage("You got " + data.timeLeftMins + " minutes left!!!");

                        if (data.timeLeftMins <= 0) {
                            p.teleport(data.from);
                            cancel();
                        }
                    }



                } else {
                    WildWarpData data = warpedPlayers.get(player.getUniqueId());

                    data.timeLeftMins -= TIMER_PERIOD_MINUTES;
                    player.sendMessage("You got " + data.timeLeftMins + " minutes left!!!");

                    if (data.timeLeftMins <= 0) {
                        player.teleport(data.from);
                        cancel();
                    }
                }



            }
        };


        // me when i deprecate!!!
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, tpTimer, 0, 20 * 60 * TIMER_PERIOD_MINUTES); // 10 minutes

        item.setAmount(item.getAmount() - 1);

        return false;
    }


}
