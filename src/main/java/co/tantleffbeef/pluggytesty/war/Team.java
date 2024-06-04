package co.tantleffbeef.pluggytesty.war;

import co.tantleffbeef.pluggytesty.goober.OfflineGoober;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Team {
    private final List<OfflineGoober> goobers;
    private final UUID teamId;

    public Team(UUID teamId, List<OfflineGoober> members) {
        this.goobers = new ArrayList<>(members);
        this.teamId = teamId;
    }

    public Team(UUID teamId) {
        this(teamId, List.of());
    }

    public void addGoober(@NotNull OfflineGoober goober) {
        goobers.add(goober);
    }

    public boolean removeGoober(@NotNull OfflineGoober goober) {
        return goobers.remove(goober);
    }

    public UUID getTeamId() {
        return teamId;
    }

    @Override
    public int hashCode() {
        return teamId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Team other))
            return false;

        return other.teamId.equals(this.teamId);
    }
}
