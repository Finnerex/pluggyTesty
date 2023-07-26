package co.tantleffbeef.pluggytesty.custom.item.utility;

import co.tantleffbeef.mcplanes.custom.item.InteractableItemType;
import co.tantleffbeef.mcplanes.custom.item.SimpleItemType;
import co.tantleffbeef.pluggytesty.expeditions.parties.Party;
import co.tantleffbeef.pluggytesty.expeditions.parties.PartyManager;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;

public class HealingAuraItemType extends SimpleItemType implements InteractableItemType {

    private final Plugin schedulerPlugin;
    private final PartyManager partyManager;
    private final int COOLDOWN_TICKS = 1200;

    public HealingAuraItemType(Plugin namespace, String id, boolean customModel, String name, PartyManager partyManager) {
        super(namespace, id, customModel, name, Material.CRIMSON_FUNGUS);
        this.schedulerPlugin = namespace;
        this.partyManager = partyManager;
    }

    @Override
    public void modifyItemMeta(@NotNull ItemMeta meta) {
        super.modifyItemMeta(meta);
        meta.setLore(Arrays.asList(ChatColor.DARK_GREEN + "Right-Click : Create a healing aura around you", ChatColor.DARK_GREEN + "Cooldown : " + COOLDOWN_TICKS / 20f + "s"));
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull ItemStack itemStack, @Nullable Block block) {
        if (player.hasCooldown(Material.CRIMSON_FUNGUS))
            return true;

        Location centerLocation = player.getEyeLocation();
        World world = centerLocation.getWorld();
        assert world != null;

        Party party = partyManager.getPartyWith(player);

        BukkitRunnable runnable = new BukkitRunnable() {

            int runs = 0; // 10 ticks per run

            @Override
            public void run() {
                if (runs > 60) { // 30 secs
                    cancel();
                    return;
                }

                Collection<Entity> entities = world.getNearbyEntities(centerLocation, 3, 40, 3);

                for (Entity e : entities) {
                    if (!(e instanceof Player p) || e.isDead()) // check if it's a player
                        continue;

                    // player is casting player or is in their party
                    if (p.getUniqueId().equals(player.getUniqueId()) || (party != null && !party.containsPlayer(p)))
                        p.setHealth(Math.min(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), player.getHealth() + 0.2));
                }

                Location drawLocation = centerLocation.clone().subtract(new Vector(3, 0, 3));

                // draw the bounds of the aura
                for (float i = 0; i < 5.8; i += 0.2) {
                    world.spawnParticle(Particle.TOTEM, drawLocation, 1, 0.2, 0, 0, 0);
                    world.spawnParticle(Particle.TOTEM, drawLocation.clone().add(0, 0, 6), 2, 0.2, 0, 0, 0);
                    drawLocation.add(0.2, 0, 0);
                }
                for (float i = 0; i < 5.8; i += 0.2) {
                    world.spawnParticle(Particle.TOTEM, drawLocation, 1, 0, 0, 0.2, 0);
                    world.spawnParticle(Particle.TOTEM, drawLocation.clone().add(-6, 0, 0), 2, 0, 0, 0.2, 0);
                    drawLocation.add(0, 0, 0.2);
                }

                runs ++;

            }
        };

        runnable.runTaskTimer(schedulerPlugin, 0, 10);

        player.setCooldown(Material.CRIMSON_FUNGUS, COOLDOWN_TICKS);

        return true;
    }
}
