package co.tantleffbeef.pluggytesty.expeditions;

public class InvalidExpeditionStateException extends Exception {
    public InvalidExpeditionStateException() {
        super();
    }

    public InvalidExpeditionStateException(String message) {
        super(message);
    }
}
