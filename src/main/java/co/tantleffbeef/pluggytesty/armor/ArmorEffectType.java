package co.tantleffbeef.pluggytesty.armor;

// an enum containing all possible unique effects a player can get from armor trims
public enum ArmorEffectType {
    CONDUIT_POWER, // on equip
    DEBUFF_DAMAGE_IMMUNITY, // on effect given to player -> if wither/poison, remove // TODO
    ARROW_CONSERVATION, // on shoot -> 50% to give back arrow
    NIGHT_VISION, // on equip, on effect given (blind/dark resist)
    JUMP_BOOST, // on equip // TODO
    HASTE, // on equip // TODO
    SPEED, // on equip // TODO
    EXP_BOOST, // on exp gained
    FIRE_RESISTANCE, // on equip
    KNOCKBACK_RESIST, // on player damage -> set kb to false
    DAMAGE_INCREASE, // on entity damage -> if player has this, increase damage done by player
    FALL_DAMAGE_IMMUNITY, // on player damage -> check if from fall -> cancel
    WITHER_ATTACKS, // on entity damage -> give entity wither effect
    DASH, // FIGURE OUT // TODO
    HEALTH_BOOST, // on equip
    REGEN_ON_KILL; // on entity death -> give player health

    @Override
    public String toString() {
        return switch (this) {
            case CONDUIT_POWER -> "conduit power and water dash";
            case DEBUFF_DAMAGE_IMMUNITY -> "immunity to wither and poison";
            case ARROW_CONSERVATION -> "50% chance to not consume arrows";
            case NIGHT_VISION -> "night vision and immunity to blindness / darkness";
            case JUMP_BOOST -> "jump boost IV";
            case HASTE -> "haste II";
            case SPEED -> "speed II";
            case EXP_BOOST -> "double EXP gain";
            case FIRE_RESISTANCE -> "fire resistance";
            case KNOCKBACK_RESIST -> "knockback resistance";
            case DAMAGE_INCREASE -> "increased damage";
            case FALL_DAMAGE_IMMUNITY -> "immunity to fall damage";
            case WITHER_ATTACKS -> "withering attacks";
            case DASH -> "a dash";
            case HEALTH_BOOST -> "increased health";
            case REGEN_ON_KILL -> "regeneration on mob kills";
        };
    }


}

