package co.tantleffbeef.pluggytesty.misc;

import org.jetbrains.annotations.NotNull;

public class InvalidJsonException extends Exception {
    public InvalidJsonException(@NotNull String reason) {
        super(reason);
    }
}
