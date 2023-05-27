package co.tantleffbeef.pluggytesty.custom.item.weapons.arrows;

import org.bukkit.entity.Arrow;

public interface CustomArrow {

    /**
     * Runs when the custom arrow is shot
     * used to modify what the projectile does when shot
     * @param arrow the projectile of the shoot event
     */
    void applySpawnEffects(Arrow arrow);


//    void applyLandingEffects(Arrow arrow);

}
