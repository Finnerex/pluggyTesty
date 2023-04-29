package co.tantleffbeef.pluggytesty.weapons;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class RandomEffectBowInteractListener implements Listener {

    @EventHandler
    private void onEntityShootBow(EntityShootBowEvent event) {

        if (event.isCancelled())
            return;

        ItemMeta meta = event.getBow().getItemMeta();

        if (meta == null || meta.getLore() == null || !meta.getLore().get(0).equals(RandomEffectBow.REB_LORE))
            return;

        Arrow arrow1 = (Arrow) event.getProjectile();

        int arrow = new Random().nextInt(6);

        // Gavin and Finn are Gonna Get Mad At Me For Hard Coding
        if (arrow == 0)
            arrow1.addCustomEffect(PotionEffectType.BLINDNESS.createEffect(100, 1), false);
        if (arrow == 1)
            arrow1.addCustomEffect(PotionEffectType.HARM.createEffect(1, 1), false);
        if (arrow == 2)
            arrow1.addCustomEffect(PotionEffectType.HARM.createEffect(1, 2), false);
        if (arrow == 3)
            arrow1.addCustomEffect(PotionEffectType.POISON.createEffect(100, 1), false);
        if (arrow == 4)
            arrow1.addCustomEffect(PotionEffectType.POISON.createEffect(40, 2), false);
        if (arrow == 5)
            arrow1.addCustomEffect(PotionEffectType.SLOW.createEffect(60, 4), false);

        event.setProjectile(arrow1);
    }
}
