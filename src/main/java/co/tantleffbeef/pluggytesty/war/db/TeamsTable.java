package co.tantleffbeef.pluggytesty.war.db;

import co.tantleffbeef.pluggytesty.goober.GooberStateController;
import co.tantleffbeef.pluggytesty.goober.OfflineGoober;
import co.tantleffbeef.pluggytesty.war.Team;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

public interface TeamsTable {
    void saveTeam(@NotNull Team team);
    @NotNull UUID getTeamWithPlayer(@NotNull OfflineGoober goober);
    @NotNull Collection<Team> loadTeams(Server server, GooberStateController gooberController);
    boolean isGooberOnTeam(@NotNull OfflineGoober goober);
}
