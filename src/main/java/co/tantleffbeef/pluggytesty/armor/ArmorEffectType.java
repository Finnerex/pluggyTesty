package co.tantleffbeef.pluggytesty.armor;

// an enum containing all possible unique effects a player can get from armor trims
public enum ArmorEffectType {
    CONDUIT_POWER,
    DEBUFF_DAMAGE_IMMUNITY,
    ARROW_CONSERVATION,
    NIGHT_VISION,
    JUMP_BOOST,
    HASTE,
    SPEED,
    EXP_BOOST,
    FIRE_RESISTANCE,
    KNOCKBACK_RESIST,
    DAMAGE_INCREASE,
    FALL_DAMAGE_IMMUNITY,
    WITHER_ATTACKS,
    DASH,
    HEALTH_BOOST,
    REGEN_ON_KILL;

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

