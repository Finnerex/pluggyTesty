package co.tantleffbeef.pluggytesty.war.db.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class SQLConnection {
    private final String uri;

    public SQLConnection(String uri) {
        this.uri = uri;
    }

    public Connection open() {
        try {
            // Connect to db
            return DriverManager.getConnection(uri);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
