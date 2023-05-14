package co.tantleffbeef.pluggytesty.attributes;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class InvalidItemKeyException extends Exception {
    private final NamespacedKey invalidKey;

    public InvalidItemKeyException(@NotNull NamespacedKey invalidKey) {
        this(invalidKey, "No item could be found with id: " + invalidKey);
    }

    public InvalidItemKeyException(@NotNull NamespacedKey invalidKey, String message) {
        super(message);
        this.invalidKey = invalidKey;
    }

    public @NotNull NamespacedKey getInvalidKey() {
        return invalidKey;
    }
}
