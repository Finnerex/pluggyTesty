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

        int arrow = new Random().nextInt(12);

        if (arrow > 5) //i.e. 6-11
            return;

        Arrow arrow1 = new Arrow() {
            @Override
            public void setBasePotionData(@NotNull PotionData potionData) {

            }

            @NotNull
            @Override
            public PotionData getBasePotionData() {
                return null;
            }

            @Nullable
            @Override
            public Color getColor() {
                return null;
            }

            @Override
            public void setColor(@Nullable Color color) {

            }

            @Override
            public boolean hasCustomEffects() {
                return false;
            }

            @NotNull
            @Override
            public List<PotionEffect> getCustomEffects() {
                return null;
            }

            @Override
            public boolean addCustomEffect(@NotNull PotionEffect potionEffect, boolean b) {
                return false;
            }

            @Override
            public boolean removeCustomEffect(@NotNull PotionEffectType potionEffectType) {
                return false;
            }

            @Override
            public boolean hasCustomEffect(@Nullable PotionEffectType potionEffectType) {
                return false;
            }

            @Override
            public void clearCustomEffects() {

            }

            @Override
            public int getKnockbackStrength() {
                return 0;
            }

            @Override
            public void setKnockbackStrength(int i) {

            }

            @Override
            public double getDamage() {
                return 0;
            }

            @Override
            public void setDamage(double v) {

            }

            @Override
            public int getPierceLevel() {
                return 0;
            }

            @Override
            public void setPierceLevel(int i) {

            }

            @Override
            public boolean isCritical() {
                return false;
            }

            @Override
            public void setCritical(boolean b) {

            }

            @Override
            public boolean isInBlock() {
                return false;
            }

            @Nullable
            @Override
            public Block getAttachedBlock() {
                return null;
            }

            @NotNull
            @Override
            public PickupStatus getPickupStatus() {
                return null;
            }

            @Override
            public void setPickupStatus(@NotNull AbstractArrow.PickupStatus pickupStatus) {

            }

            @Override
            public boolean isShotFromCrossbow() {
                return false;
            }

            @Override
            public void setShotFromCrossbow(boolean b) {

            }

            @Nullable
            @Override
            public ProjectileSource getShooter() {
                return null;
            }

            @Override
            public void setShooter(@Nullable ProjectileSource projectileSource) {

            }

            @Override
            public boolean doesBounce() {
                return false;
            }

            @Override
            public void setBounce(boolean b) {

            }

            @NotNull
            @Override
            public Location getLocation() {
                return null;
            }

            @Nullable
            @Override
            public Location getLocation(@Nullable Location location) {
                return null;
            }

            @Override
            public void setVelocity(@NotNull Vector vector) {

            }

            @NotNull
            @Override
            public Vector getVelocity() {
                return null;
            }

            @Override
            public double getHeight() {
                return 0;
            }

            @Override
            public double getWidth() {
                return 0;
            }

            @NotNull
            @Override
            public BoundingBox getBoundingBox() {
                return null;
            }

            @Override
            public boolean isOnGround() {
                return false;
            }

            @Override
            public boolean isInWater() {
                return false;
            }

            @NotNull
            @Override
            public World getWorld() {
                return null;
            }

            @Override
            public void setRotation(float v, float v1) {

            }

            @Override
            public boolean teleport(@NotNull Location location) {
                return false;
            }

            @Override
            public boolean teleport(@NotNull Location location, @NotNull PlayerTeleportEvent.TeleportCause teleportCause) {
                return false;
            }

            @Override
            public boolean teleport(@NotNull Entity entity) {
                return false;
            }

            @Override
            public boolean teleport(@NotNull Entity entity, @NotNull PlayerTeleportEvent.TeleportCause teleportCause) {
                return false;
            }

            @NotNull
            @Override
            public List<Entity> getNearbyEntities(double v, double v1, double v2) {
                return null;
            }

            @Override
            public int getEntityId() {
                return 0;
            }

            @Override
            public int getFireTicks() {
                return 0;
            }

            @Override
            public int getMaxFireTicks() {
                return 0;
            }

            @Override
            public void setFireTicks(int i) {

            }

            @Override
            public void setVisualFire(boolean b) {

            }

            @Override
            public boolean isVisualFire() {
                return false;
            }

            @Override
            public int getFreezeTicks() {
                return 0;
            }

            @Override
            public int getMaxFreezeTicks() {
                return 0;
            }

            @Override
            public void setFreezeTicks(int i) {

            }

            @Override
            public boolean isFrozen() {
                return false;
            }

            @Override
            public void remove() {

            }

            @Override
            public boolean isDead() {
                return false;
            }

            @Override
            public boolean isValid() {
                return false;
            }

            @NotNull
            @Override
            public Server getServer() {
                return null;
            }

            @Override
            public boolean isPersistent() {
                return false;
            }

            @Override
            public void setPersistent(boolean b) {

            }

            @Nullable
            @Override
            public Entity getPassenger() {
                return null;
            }

            @Override
            public boolean setPassenger(@NotNull Entity entity) {
                return false;
            }

            @NotNull
            @Override
            public List<Entity> getPassengers() {
                return null;
            }

            @Override
            public boolean addPassenger(@NotNull Entity entity) {
                return false;
            }

            @Override
            public boolean removePassenger(@NotNull Entity entity) {
                return false;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean eject() {
                return false;
            }

            @Override
            public float getFallDistance() {
                return 0;
            }

            @Override
            public void setFallDistance(float v) {

            }

            @Override
            public void setLastDamageCause(@Nullable EntityDamageEvent entityDamageEvent) {

            }

            @Nullable
            @Override
            public EntityDamageEvent getLastDamageCause() {
                return null;
            }

            @NotNull
            @Override
            public UUID getUniqueId() {
                return null;
            }

            @Override
            public int getTicksLived() {
                return 0;
            }

            @Override
            public void setTicksLived(int i) {

            }

            @Override
            public void playEffect(@NotNull EntityEffect entityEffect) {

            }

            @NotNull
            @Override
            public EntityType getType() {
                return null;
            }

            @NotNull
            @Override
            public Sound getSwimSound() {
                return null;
            }

            @NotNull
            @Override
            public Sound getSwimSplashSound() {
                return null;
            }

            @NotNull
            @Override
            public Sound getSwimHighSpeedSplashSound() {
                return null;
            }

            @Override
            public boolean isInsideVehicle() {
                return false;
            }

            @Override
            public boolean leaveVehicle() {
                return false;
            }

            @Nullable
            @Override
            public Entity getVehicle() {
                return null;
            }

            @Override
            public void setCustomNameVisible(boolean b) {

            }

            @Override
            public boolean isCustomNameVisible() {
                return false;
            }

            @Override
            public void setVisibleByDefault(boolean b) {

            }

            @Override
            public boolean isVisibleByDefault() {
                return false;
            }

            @Override
            public void setGlowing(boolean b) {

            }

            @Override
            public boolean isGlowing() {
                return false;
            }

            @Override
            public void setInvulnerable(boolean b) {

            }

            @Override
            public boolean isInvulnerable() {
                return false;
            }

            @Override
            public boolean isSilent() {
                return false;
            }

            @Override
            public void setSilent(boolean b) {

            }

            @Override
            public boolean hasGravity() {
                return false;
            }

            @Override
            public void setGravity(boolean b) {

            }

            @Override
            public int getPortalCooldown() {
                return 0;
            }

            @Override
            public void setPortalCooldown(int i) {

            }

            @NotNull
            @Override
            public Set<String> getScoreboardTags() {
                return null;
            }

            @Override
            public boolean addScoreboardTag(@NotNull String s) {
                return false;
            }

            @Override
            public boolean removeScoreboardTag(@NotNull String s) {
                return false;
            }

            @NotNull
            @Override
            public PistonMoveReaction getPistonMoveReaction() {
                return null;
            }

            @NotNull
            @Override
            public BlockFace getFacing() {
                return null;
            }

            @NotNull
            @Override
            public Pose getPose() {
                return null;
            }

            @NotNull
            @Override
            public SpawnCategory getSpawnCategory() {
                return null;
            }

            @NotNull
            @Override
            public Spigot spigot() {
                return null;
            }

            @Nullable
            @Override
            public String getCustomName() {
                return null;
            }

            @Override
            public void setCustomName(@Nullable String s) {

            }

            @Override
            public void sendMessage(@NotNull String s) {

            }

            @Override
            public void sendMessage(@NotNull String... strings) {

            }

            @Override
            public void sendMessage(@Nullable UUID uuid, @NotNull String s) {

            }

            @Override
            public void sendMessage(@Nullable UUID uuid, @NotNull String... strings) {

            }

            @NotNull
            @Override
            public String getName() {
                return null;
            }

            @Override
            public void setMetadata(@NotNull String s, @NotNull MetadataValue metadataValue) {

            }

            @NotNull
            @Override
            public List<MetadataValue> getMetadata(@NotNull String s) {
                return null;
            }

            @Override
            public boolean hasMetadata(@NotNull String s) {
                return false;
            }

            @Override
            public void removeMetadata(@NotNull String s, @NotNull Plugin plugin) {

            }

            @Override
            public boolean isPermissionSet(@NotNull String s) {
                return false;
            }

            @Override
            public boolean isPermissionSet(@NotNull Permission permission) {
                return false;
            }

            @Override
            public boolean hasPermission(@NotNull String s) {
                return false;
            }

            @Override
            public boolean hasPermission(@NotNull Permission permission) {
                return false;
            }

            @NotNull
            @Override
            public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b) {
                return null;
            }

            @NotNull
            @Override
            public PermissionAttachment addAttachment(@NotNull Plugin plugin) {
                return null;
            }

            @Nullable
            @Override
            public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b, int i) {
                return null;
            }

            @Nullable
            @Override
            public PermissionAttachment addAttachment(@NotNull Plugin plugin, int i) {
                return null;
            }

            @Override
            public void removeAttachment(@NotNull PermissionAttachment permissionAttachment) {

            }

            @Override
            public void recalculatePermissions() {

            }

            @NotNull
            @Override
            public Set<PermissionAttachmentInfo> getEffectivePermissions() {
                return null;
            }

            @Override
            public boolean isOp() {
                return false;
            }

            @Override
            public void setOp(boolean b) {

            }

            @NotNull
            @Override
            public PersistentDataContainer getPersistentDataContainer() {
                return null;
            }
        };

        // Gavin and Finn are Gonna Get Mad At Me For Hard Coding
        if (arrow == 0)
            arrow1.addCustomEffect(PotionEffectType.BLINDNESS.createEffect(100, 1), true);
        if (arrow == 1)
            arrow1.addCustomEffect(PotionEffectType.HARM.createEffect(1, 1), true);
        if (arrow == 2)
            arrow1.addCustomEffect(PotionEffectType.HARM.createEffect(1, 2), true);
        if (arrow == 3)
            arrow1.addCustomEffect(PotionEffectType.POISON.createEffect(100, 1), true);
        if (arrow == 4)
            arrow1.addCustomEffect(PotionEffectType.POISON.createEffect(40, 2), true);
        if (arrow == 5)
            arrow1.addCustomEffect(PotionEffectType.SLOW.createEffect(60, 4), true);

        event.setProjectile(arrow1);
    }
}
