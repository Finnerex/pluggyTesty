package co.tantleffbeef.pluggytesty.custom.item.weapons.arrows;

import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.ProjectileHitEvent;

public interface CustomArrow {

    /**
     * Runs when the custom arrow is shot
     * used to modify what the projectile does when shot
     * @param arrow the projectile of the shoot event
     */
    void applySpawnEffects(Arrow arrow);


    /**
     * Runs when the custom arrow lands
     * @param arrow the arrow in question
     * @param event the land/hit event (I don't want to pass this in but balls)
     */
    void applyLandingEffects(Arrow arrow, ProjectileHitEvent event);

}
