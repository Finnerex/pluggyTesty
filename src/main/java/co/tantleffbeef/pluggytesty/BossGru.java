package co.tantleffbeef.pluggytesty;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

            double prevHealth = 500;
            double currHealth = 500;
            @Override
            public void run() {

                Location eye = gru.getEyeLocation();

                if (gru.isDead()) { //dead command
                    for (int i = 0; i < 15; i++) {
                        ((Zombie) gru.getWorld().spawnEntity(eye, EntityType.ZOMBIE)).setAge(1);
                    }
                    gru.getWorld().playSound(eye, Sound.ENTITY_PLAYER_LEVELUP, 20, 0.1f);

                    cancel();
                    return;
                }

                currHealth = gru.getHealth();


                if (currHealth < prevHealth){ //spawns zombie hoard on hit

                    for (int i = 0; i < 5; i++){

                        Zombie swordZombie = (Zombie) w.spawnEntity(eye, EntityType.ZOMBIE);
                        EntityEquipment equipment = swordZombie.getEquipment();
                        equipment.setItemInMainHand(new ItemStack(Material.IRON_SWORD));
                        equipment.setHelmet(new ItemStack(Material.LEATHER_HELMET));
                        equipment.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                        equipment.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                        equipment.setBoots(new ItemStack(Material.LEATHER_BOOTS));
                    }

                    prevHealth = currHealth;
                }

            }
        };
        runnable.runTaskTimer(plugin, 0, 1);

        return true;
    }


}
