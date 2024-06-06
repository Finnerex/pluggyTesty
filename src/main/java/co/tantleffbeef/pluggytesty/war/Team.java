package co.tantleffbeef.pluggytesty.war;

import co.tantleffbeef.pluggytesty.goober.Goober;
import co.tantleffbeef.pluggytesty.goober.OfflineGoober;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Team {
    private final List<OfflineGoober> goobers;
    private final OfflineGoober owner;
    private final UUID teamId;
    private final String name;

    public Team(UUID teamId, String name, OfflineGoober owner, List<OfflineGoober> members) {
        this.goobers = new ArrayList<>(members);
        this.owner = owner;
        this.teamId = teamId;
        this.name = name;
    }

    public Team(UUID teamId, String name, OfflineGoober owner) {
        this(teamId, name, owner, List.of(owner));
    }

    public void addGoober(@NotNull OfflineGoober goober) {
        // Add to list
        goobers.add(goober);

        // Send message
        for (OfflineGoober og : goobers) {
            if (!og.isOnline())
                continue;

            var g = (Goober) og;
            g.asPlayer().spigot().sendMessage(
                    new ComponentBuilder("---").color(ChatColor.RED)
                            .append(goober.asOfflinePlayer().getName()).color(ChatColor.YELLOW)
                            .append(" has been added to the team!\n").color(ChatColor.GREEN)
                            .append("---").color(ChatColor.RED).build()
            );
        }
    }

    public boolean removeGoober(@NotNull OfflineGoober goober) {
        return goobers.remove(goober);
    }

    public boolean isGooberMember(@NotNull OfflineGoober goober) {
        return goobers.contains(goober);
    }

    public UUID getTeamId() {
        return teamId;
    }

    public List<OfflineGoober> getTeamMembers() {
        return List.copyOf(goobers);
    }

    public OfflineGoober getTeamOwner() {
        return owner;
    }

    public String getTeamName() {
        return name;
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
