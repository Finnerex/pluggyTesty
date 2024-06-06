package co.tantleffbeef.pluggytesty.war;

import co.tantleffbeef.pluggytesty.goober.GooberStateController;
import co.tantleffbeef.pluggytesty.goober.OfflineGoober;
import co.tantleffbeef.pluggytesty.war.db.InvalidDatabaseException;
import co.tantleffbeef.pluggytesty.war.db.TeamsTable;
import org.bukkit.Server;

import java.util.*;

public class TeamContainer {
    private final TeamsTable teamsTable;
    private final List<Team> teams;
    private final Set<UUID> teamIds;
    private final Map<UUID, Team> teamIdMap;

    public static TeamContainer fromDatabaseTable(TeamsTable teamsTable, Server bukkitServer, GooberStateController gooberController) {
        return new TeamContainer(teamsTable, new ArrayList<>(teamsTable.loadTeams(bukkitServer, gooberController)));
    }

    private TeamContainer(TeamsTable teamsTable, ArrayList<Team> teams) {
        this.teamsTable = teamsTable;
        this.teams = teams;
        this.teamIds = new HashSet<>();
        this.teamIdMap = new HashMap<>();

        for (Team t : teams) {
            // add all team ids to the set
            teamIds.add(t.getTeamId());
            teamIdMap.put(t.getTeamId(), t);
        }
    }

    public Team createTeam(String name, OfflineGoober owner) {
        UUID newTeamId;
        Team newTeam;

        // Find unused team id
        do {
            newTeamId = UUID.randomUUID();
        } while (teamIds.contains(newTeamId));

        // create team and add to list
        newTeam = new Team(newTeamId, name, owner);
        teams.add(newTeam);
        teamIds.add(newTeamId);
        teamIdMap.put(newTeamId, newTeam);

        return newTeam;
    }

    public void saveTeam(Team team) {
        teamsTable.saveTeam(team);
    }

    public Team getTeamWithGoober(OfflineGoober goober) {
        // grab team uuid from table
        var teamId = teamsTable.getTeamWithPlayer(goober);

        // grab team with id
        var team = teamIdMap.get(teamId);

        // null check
        if (team == null)
            throw new InvalidDatabaseException("Database contains player " + goober.asOfflinePlayer().getName() + " on null team");

        return team;
    }

    public boolean gooberOnTeam(OfflineGoober goober) {
        return teamsTable.isGooberOnTeam(goober);
    }
}
