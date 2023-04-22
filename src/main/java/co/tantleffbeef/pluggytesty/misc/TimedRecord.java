package co.tantleffbeef.pluggytesty.misc;

import java.time.Instant;

public record TimedRecord<T>(Instant time, T object) {
    public static <O> TimedRecord<O> now(O obj) {
        return new TimedRecord<>(Instant.now(), obj);
    }
}
