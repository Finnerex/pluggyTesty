package co.tantleffbeef.pluggytesty.war.db.sqlite;

import co.tantleffbeef.pluggytesty.war.db.ClaimsTable;
import co.tantleffbeef.pluggytesty.war.db.TeamsTable;
import co.tantleffbeef.pluggytesty.war.db.WarDatabase;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.sql.SQLException;

/**
 * Implements war database in sqlite
 * <br><br>
 * {@code teams} table:
 * <pre>{@code
 * +--------------------------------------------------------------+
 * | id char(36) PRIMARY KEY | name varchar(255) | owner char(36) |
 * +--------------------------------------------------------------+
 *   ^team uuid                ^team name          ^owner uuid
 * }</pre>
 * {@code players} table:
 * <pre>{@code
 * +-----------------------------------------+
 * | id char(36) PRIMARY KEY | team char(36) |
 * +-----------------------------------------+
 *   ^player uuid              ^team uuid
 * }</pre>
 * {@code claims} table:
 * <pre>{@code
 * +---------------------------------------+
 * | x integer | y integer | team char(36) |
 * +---------------------------------------+
 *   ^x coord    ^y coord    ^claim team uuid
 * }</pre>
 */
public class SQLiteWarDatabase implements WarDatabase {
    private final SQLiteTeamsTable teamsTable;
    private final SQLiteClaimsTable claimsTable;

    public static SQLiteWarDatabase fromPath(Path databasePath) {
        String databaseUrl = "jdbc:sqlite:" + databasePath.toAbsolutePath();

        return new SQLiteWarDatabase(new SQLConnection(databaseUrl));
    }

    private SQLiteWarDatabase(SQLConnection dbConnection) {
        this.teamsTable = new SQLiteTeamsTable(dbConnection);
        this.claimsTable = new SQLiteClaimsTable();

        // create tables
        try (var conn = dbConnection.open();
             var stmt = conn.createStatement()) {
            // teams table
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS teams (" +
                            "   id char(36) NOT NULL PRIMARY KEY," +
                            "   name varchar(255)," +
                            "   owner char(36)" +
                            ");"
            );

            // players table (stores team)
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS players (" +
                            "   id char(36) NOT NULL PRIMARY KEY," +
                            "   team char(36)" +
                            ");"
            );

            // claims table
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS claims (" +
                            "   x integer," +
                            "   y integer," +
                            "   team char(36)" +
                            ");"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull TeamsTable getTeamsTable() {
        return this.teamsTable;
    }

    @Override
    public @NotNull ClaimsTable getClaimsTable() {
        return claimsTable;
    }
}
