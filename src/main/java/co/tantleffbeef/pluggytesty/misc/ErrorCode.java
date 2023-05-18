package co.tantleffbeef.pluggytesty.misc;

public enum ErrorCode {
    PLAYER_ESCAPED_EXPEDITION_ROOM(1)

    ;

    private final int errorCode;

    ErrorCode(int code) {
        this.errorCode = code;
    }

    @Override
    public String toString() {
        return String.format("[#%04d]", errorCode);
    }
}
