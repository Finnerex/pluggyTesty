package co.tantleffbeef.pluggytesty.armor;

public enum ArmorEffectType {
    NONE, // idk because i have to put something in the else
    CONDUIT_POWER, // on equip
    DEBUFF_DAMAGE_IMMUNITY, // on effect given to player -> if wither/poison, remove
    ARROW_CONSERVATION, // on shoot -> 50% to give back arrow
    NIGHT_VISION, // on equip, on effect given (blind/dark resist)
    JUMP_BOOST, // on equip
    HASTE, // on equip
    SPEED, // on equip
    EXP_BOOST, // on exp gained
    FIRE_RESISTANCE, // on equip
    HUNGER_CONSERVATION, // on hunger reduced -> if not from healing !!Cant differentiate reason for food change!!
    DAMAGE_INCREASE, // on entity damage -> if player has this, increase damage done by player
    FALL_DAMAGE_IMMUNITY, // FIGURE OUT
    WITHER_ATTACKS, // on entity damage -> give entity wither effect
    DASH, // FIGURE OUT
    HEALTH_BOOST, // on equip
    REGEN_ON_KILL // on entity death -> give player health

}
