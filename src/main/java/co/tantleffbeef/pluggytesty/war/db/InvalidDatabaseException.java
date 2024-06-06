package co.tantleffbeef.pluggytesty.war.db;

public class InvalidDatabaseException extends RuntimeException {
    public InvalidDatabaseException(String message) {
        super(message);
    }
}
