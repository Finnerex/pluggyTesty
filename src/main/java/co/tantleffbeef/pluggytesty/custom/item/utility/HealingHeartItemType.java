package co.tantleffbeef.pluggytesty.custom.item.utility;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class HealingHeartItemType extends SimpleItemType implements InteractableItemType {

    private final Plugin schedulerPlugin;

    public HealingHeartItemType(Plugin namespace, String id, boolean customModel, String name) {
        super(namespace, id, customModel, name, Material.REDSTONE);
        this.schedulerPlugin = namespace;
    }

    @Override
    public void interact(@NotNull Player player, @NotNull ItemStack item, Block block) {
        if (player.hasCooldown(Material.REDSTONE))
            return;

        int amount = item.getAmount();
        // make runnable at beginning, if charge has not incremented in x ticks, heal and reset

        if(amount == 1) {
            BukkitRunnable runnable = new BukkitRunnable() {
                private int tickNum = 0;
                private int lastAmount = 1;

                @Override
                public void run() {
                    tickNum ++;
                    int curAmount = item.getAmount();

                    if(curAmount != lastAmount)
                        tickNum = 0;

                    if (tickNum > 15) { // last and current amount are the same for 15 ticks
                        heal(player, item);
                        cancel();
                        return;
                    }

                    lastAmount = curAmount;

                }

            };

            runnable.runTaskTimer(schedulerPlugin, 0, 0);

        }

        item.setAmount(Math.min(amount + 1, 10)); //every tick(s) it is held for, max 60
        player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1 + item.getAmount() * 0.01f);
    }

    private void heal(Player player, ItemStack item) {
        if (player.isDead())
            return;

        player.playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

        player.setHealth(Math.min(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), (player.getHealth() + item.getAmount())));
        player.setCooldown(Material.REDSTONE, 60);

        item.setAmount(1);
    }

}
