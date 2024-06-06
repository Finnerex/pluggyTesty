package co.tantleffbeef.pluggytesty.war.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.tantleffbeef.pluggytesty.goober.Goober;
import co.tantleffbeef.pluggytesty.goober.GooberStateController;
import co.tantleffbeef.pluggytesty.war.ClaimWorldController;
import co.tantleffbeef.pluggytesty.war.Team;
import co.tantleffbeef.pluggytesty.war.TeamContainer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@CommandAlias("team|t")
@SuppressWarnings("unused")
public class TeamCommand extends BaseCommand {
    private final ClaimWorldController claimWorldController;
    private final TeamContainer teamContainer;
    private final GooberStateController gooberController;
    private final Map<UUID, List<Team>> invites;

    public TeamCommand(ClaimWorldController claimWorldController, TeamContainer teamcontainer, GooberStateController gooberController) {
        this.claimWorldController = claimWorldController;
        this.teamContainer = teamcontainer;
        this.gooberController = gooberController;
        this.invites = new HashMap<>();
    }

    @HelpCommand
    public void onHelp(@NotNull Player caller) {
        caller.performCommand("help pluggytesty:team");
    }

    @Subcommand("create")
    public void onCreate(@NotNull Player caller, String name) {
        var goober = gooberController.wrapPlayer(caller);
        if (teamContainer.gooberOnTeam(goober)) {
            caller.sendMessage(ChatColor.RED + "You're already on a team!");
            return;
        }

        var newTeam = teamContainer.createTeam(name, goober);
        teamContainer.saveTeam(newTeam);

        caller.spigot().sendMessage(
                new ComponentBuilder("---\n").color(ChatColor.RED)
                        .append("You have successfully created the team ").color(ChatColor.GREEN)
                        .append(name).color(ChatColor.YELLOW)
                        .append("!\n").color(ChatColor.GREEN)
                        .append("---").color(ChatColor.RED)
                        .build()
        );
    }

    @Subcommand("invite")
    public void onInvite(@NotNull Player caller, @NotNull OfflinePlayer other) {
        var inviter = gooberController.wrapPlayer(caller);
        var invited = gooberController.wrapOfflinePlayer(other);

        // Check if players are on teams
        if (!teamContainer.gooberOnTeam(inviter)) {
            caller.sendMessage(ChatColor.RED + "You are not on a team!");
            return;
        }

        if (teamContainer.gooberOnTeam(invited)) {
            caller.sendMessage(ChatColor.RED + "That player is already on a team!");
            return;
        }

        // Check if inviter is owner
        var team = teamContainer.getTeamWithGoober(inviter);
        if (!team.getTeamOwner().equals(inviter)) {
            caller.sendMessage("You are not the owner of the team!");
            return;
        }

        // Send invite
        if (!invites.containsKey(invited.getUniqueId())) {
            invites.put(invited.getUniqueId(), new ArrayList<>());
        }

        var inviteList = invites.get(invited.getUniqueId());
        inviteList.add(team);

        caller.spigot().sendMessage(
                new ComponentBuilder("---\n").color(ChatColor.RED)
                        .append(invited.asOfflinePlayer().getName()).color(ChatColor.YELLOW)
                        .append(" has been invited to the team.\n").color(ChatColor.GREEN)
                        .append("---").color(ChatColor.RED)
                        .build()
        );

        // send invite message
        if (invited.isOnline()) {
            var invitedOnline = (Goober) invited;

            invitedOnline.asPlayer().spigot().sendMessage(
                    new ComponentBuilder("---\n").color(ChatColor.RED)
                            .append("You have been invited to the team ").color(ChatColor.GREEN)
                            .append(team.getTeamName()).color(ChatColor.YELLOW)
                            .append(".\n").color(ChatColor.GREEN)
                            .append("To join, type ").color(ChatColor.GREEN)
                            .append("/team accept ").color(ChatColor.YELLOW)
                            .append(team.getTeamName()).color(ChatColor.YELLOW)
                            .append("\n")
                            .append("---").color(ChatColor.RED)
                            .build()
            );
        }
    }

    @Subcommand("accept")
    public void onAccept(@NotNull Player caller, @NotNull String teamName) {
        var acceptor = gooberController.wrapPlayer(caller);

        // Check if already on a team
        if (teamContainer.gooberOnTeam(acceptor)) {
            caller.sendMessage(ChatColor.RED + "You are already on a team!");
            return;
        }

        // grab their invites
        List<Team> theirInvites = invites.get(acceptor.getUniqueId());
        if (theirInvites == null) {
            caller.sendMessage(ChatColor.RED + "You don't have any invites!");
            return;
        }

        // find the team they are accepting
        Team acceptTeam = null;
        for (Team t : theirInvites) {
            if (t.getTeamName().equals(teamName)) {
                acceptTeam = t;
                break;
            }
        }

        // If it's not in the list
        if (acceptTeam == null) {
            caller.sendMessage(ChatColor.RED + "You weren't invited to the team " + teamName);
            return;
        }

        // accept the invite
        acceptTeam.addGoober(acceptor); // message is included in this
        invites.remove(acceptor.getUniqueId());
    }
}
