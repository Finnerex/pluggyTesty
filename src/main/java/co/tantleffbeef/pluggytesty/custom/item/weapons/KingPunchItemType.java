package co.tantleffbeef.pluggytesty.custom.item.weapons;


import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class KingPunchItemType extends SimpleItemType implements InteractableItemType {

    private final Plugin schedulerPlugin;

    public KingPunchItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.RED_WOOL);
        this.schedulerPlugin = namespace;
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack item, Block block) {
        if (player.hasCooldown(Material.RED_WOOL)) {
            return false;
        }

        item.setAmount(Math.min(item.getAmount() + 1, 50)); // every tick(s) it is held for, max 60

        // make runnable at beginning, if charge has not incremented in 15 ticks, heal and reset
        if (item.getAmount() == 2) {
            BukkitRunnable runnable = new BukkitRunnable() {
                private int tickNum = 0;
                private int lastAmount = 1;

                @Override
                public void run() {
                    tickNum++;
                    int curAmount = item.getAmount();

                    if (curAmount != lastAmount)
                        tickNum = 0;

                    if (tickNum > 15) { // last and current amount are the same for 15 ticks
                        cancel();
                        if (item.getAmount() < 50) {
                            item.setAmount(1);
                            return;
                        }
                            explode(player, item);
                            return;
                    }

                    lastAmount = curAmount;

                }

            };
            runnable.runTaskTimer(schedulerPlugin, 0, 0);

        }
            return false;
    }
    public void explode (Player player, ItemStack item) {
        Location location = player.getEyeLocation();
        World world = player.getWorld();
        Vector direction = location.getDirection();
        location.add(direction.clone().multiply(7));

        BukkitRunnable runnable = new BukkitRunnable() {
            int tickNum = 0;

            @Override
            public void run() {
                location.add(direction);
                world.createExplosion(location, 4, false, false);
                tickNum++;

                if (tickNum > 18) {
                    cancel();
                }
            }
        };
        runnable.runTaskTimer(schedulerPlugin, 0, 0);
        item.setAmount(1);
        player.setCooldown(Material.RED_WOOL, 10);
    }
}