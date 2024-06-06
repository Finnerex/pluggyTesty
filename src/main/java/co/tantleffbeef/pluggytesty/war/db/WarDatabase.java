package co.tantleffbeef.pluggytesty.war.db;

import org.jetbrains.annotations.NotNull;

public interface WarDatabase {
    @NotNull TeamsTable getTeamsTable();
}
