package co.tantleffbeef.pluggytesty;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class BossGru implements CommandExecutor {

    private final Plugin plugin;
    public BossGru(Plugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player))
            return false;

        Villager gru = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
        gru.setProfession(Villager.Profession.ARMORER);
        gru.setCustomName(ChatColor.LIGHT_PURPLE + "GRUUUUUUU");
        gru.setCustomNameVisible(true);
        gru.setPersistent(true);


        World w = gru.getWorld();


        gru.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(500);
        gru.setHealth(500);


        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {

                location eye = gru.getEyeLocation();

                if (gru.isDead())
                    gru.getWorld().spawnEntity(eye, EntityType.)
                    cancel();
                    return;

            }
        }

        return true;
    }


}
