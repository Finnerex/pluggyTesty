package co.tantleffbeef.pluggytesty.war.db.sqlite;

import co.tantleffbeef.pluggytesty.goober.GooberStateController;
import co.tantleffbeef.pluggytesty.goober.OfflineGoober;
import co.tantleffbeef.pluggytesty.war.Team;
import co.tantleffbeef.pluggytesty.war.db.TeamsTable;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class SQLiteTeamsTable implements TeamsTable {
    private final SQLConnection dbConnection;

    SQLiteTeamsTable(SQLConnection connection) {
        this.dbConnection = connection;
    }

    private static OfflineGoober gooberFromUUIDString(String uuid, GooberStateController controller) {
        UUID id = UUID.fromString(uuid);
        OfflinePlayer player = Bukkit.getOfflinePlayer(id);

        return controller.wrapOfflinePlayer(player);
    }

    private Team createTeamFromResult(ResultSet currentResult, GooberStateController gooberController) throws SQLException {
        UUID id = UUID.fromString(currentResult.getString("id"));
        String name = currentResult.getString("name");
        OfflineGoober owner = gooberFromUUIDString(currentResult.getString("owner"), gooberController);

        ArrayList<OfflineGoober> players = new ArrayList<>();

        // grab players on team
        try (var conn = dbConnection.open();
             Statement query = conn.createStatement()) {
            var result = query.executeQuery("SELECT id FROM players WHERE team='" + id + "';");

            while (result.next()) {
                var playerIdString = result.getString(0);
                var playerId = UUID.fromString(playerIdString);
                var player = Bukkit.getOfflinePlayer(playerId);
                var goober = gooberController.wrapOfflinePlayer(player);

                players.add(goober);
            }
        }

        return new Team(id, name, owner, players);
    }

    @Override
    public void saveTeam(@NotNull Team team) {
        // drop all team players
        try (var conn = dbConnection.open();
             Statement stmt = conn.createStatement()) {
            stmt.execute(
                    "INSERT INTO teams (id, name, owner) " + "VALUES (" +
                            team.getTeamId() + ", " +
                            team.getTeamName() + ", " +
                            team.getTeamOwner().getUniqueId() + ") " +
                            "ON CONFLICT (id) DO" +
                            "UPDATE SET (name, owner) = (" +
                            team.getTeamName() + ", " +
                            team.getTeamOwner().getUniqueId() + ");");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull Optional<UUID> getTeamWithPlayer(@NotNull OfflineGoober goober) {
        try (var conn = dbConnection.open();
            var stmt = conn.createStatement()) {
            var result = stmt.executeQuery("SELECT team FROM players WHERE id = \"" + goober.getUniqueId() + "\";");

            if (result.first()) {
                return Optional.of(UUID.fromString(result.getString("team")));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull Collection<Team> loadTeams(Server server, GooberStateController gooberController) {
        ArrayList<Team> teams = new ArrayList<>();

        // Execute statement to query all teams
        try (var conn = dbConnection.open();
             Statement stmt = conn.createStatement()) {
            var teamResult = stmt.executeQuery("SELECT id, name, owner FROM teams;");

            // loop through every team
            while (teamResult.next()) {
                teams.add(createTeamFromResult(teamResult, gooberController));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Return the new collection
        return teams;
    }

    @Override
    public boolean isGooberOnTeam(@NotNull OfflineGoober goober) {
        try (var conn = dbConnection.open();
             var stmt = conn.createStatement()) {
            var result = stmt.executeQuery("SELECT 1 FROM TABLE players where id = \"" + goober.getUniqueId() + "\";");
            return result.first();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
