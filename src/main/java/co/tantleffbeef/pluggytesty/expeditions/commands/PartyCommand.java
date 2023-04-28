package co.tantleffbeef.pluggytesty.expeditions.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import co.tantleffbeef.pluggytesty.expeditions.Party;
import co.tantleffbeef.pluggytesty.expeditions.PartyManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@CommandAlias("party|p")
@SuppressWarnings("unused")
public class PartyCommand extends BaseCommand {
    private final static BaseComponent[] NOT_OWNER_ERROR =
            new ComponentBuilder("You are not the owner of the party!")
                    .color(ChatColor.RED)
                    .create();


    private final Plugin plugin;
    private final Server server;
    private final PartyManager partyManager;
    private final Map<UUID, Queue<Party>> invites;
    private final long expirationTimeSeconds;

    public PartyCommand(Plugin plugin, Server server, PartyManager partyManager, long expirationTimeSeconds) {
        this.partyManager = partyManager;
        this.server = server;
        this.invites = new HashMap<>();
        this.expirationTimeSeconds = expirationTimeSeconds;
        this.plugin = plugin;
    }

    @HelpCommand
    public void onHelp(@NotNull Player caller) {
        caller.performCommand("help pluggytesty:party");
    }

    @Default
    public void onDefaultInvite(@NotNull Player caller, @NotNull OnlinePlayer toInvite) {
        onInvite(caller, toInvite);
    }

    @Subcommand("invite")
    public void onInvite(@NotNull Player caller, @NotNull OnlinePlayer toInvite) {
        final var invitee = toInvite.getPlayer();
        if (caller.equals(invitee)) {
            caller.sendMessage(ChatColor.RED + "You can't invite yourself to a party!");
            return;
        }

        final var party = partyManager.getPartyWith(caller);

        // If player is not in a party, create one for them
        // and recall function
        if (party == null) {
            createPartyOwnedBy(caller);
            onInvite(caller, toInvite);
            return;
        }

        // Check if party owner
        if (!party.partyOwner().equals(caller)) {
            caller.spigot().sendMessage(NOT_OWNER_ERROR);
            return;
        }

        if (partyManager.getPartyWith(invitee) != null) {
            caller.sendMessage(ChatColor.RED + "That player is already in a party!");
            return;
        }

        final var inviteeName = invitee.getDisplayName();
        final var inviteeUuid = invitee.getUniqueId();

        // Send message to caller
        party.broadcastMessage(
                new ComponentBuilder("\n")
                        .append(inviteeName).color(ChatColor.YELLOW)
                        .append(" has been invited to the party.\n").color(ChatColor.GOLD)
                        .create()
        );

        // Send invite to invitee
        if (!invites.containsKey(inviteeUuid))
            invites.put(inviteeUuid, new ArrayDeque<>());
        invites.get(inviteeUuid).add(party);

        // Link player can click to accept invite
        final var clickHereMessage = new TextComponent("Click here");
        clickHereMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new Text(new ComponentBuilder("Accept ").color(ChatColor.GOLD)
                        .append(caller.getDisplayName())
                        .append("'s invite")
                        .create())));
        clickHereMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                "/party accept " + caller.getName()));


        invitee.spigot().sendMessage(
                new ComponentBuilder("\n")
                        .append(caller.getDisplayName()).color(ChatColor.YELLOW)
                        .append(" sent you an invite to their party.\n").color(ChatColor.GOLD)
                        .append(clickHereMessage).color(ChatColor.YELLOW)
                        .append(" to accept.\n").color(ChatColor.GOLD)
                        .create()
        );

        // Put expiration on invite
        server.getScheduler().runTaskLater(plugin, () -> {
            if (!invites.containsKey(inviteeUuid))
                return;

            // grab all of their invites
            final var inviteList = invites.get(inviteeUuid);
            // if they don't have any then no need to expire it
            if (inviteList.size() < 1)
                return;
            if (!inviteList.peek().equals(party))
                return;

            // Pop the invite off of their invite list
            // and let them know the invitation expired

            // Send message to party
            final var inviteParty = inviteList.remove();
            inviteParty.broadcastMessage(
                    new ComponentBuilder("\n")
                            .append(invitee.getDisplayName()).color(ChatColor.YELLOW)
                            .append(" did not accept the invitation in time, so it expired.").color(ChatColor.GOLD)
                            .create()
            );

            // Send message to invited player
            final var player = server.getPlayer(inviteeUuid);
            if (player == null)
                return;

            player.spigot().sendMessage(
                    new ComponentBuilder("\nYour party invitation from ").color(ChatColor.GOLD)
                            .append(inviteParty.partyOwner().getName()).color(ChatColor.YELLOW)
                            .append(" has expired.\n").color(ChatColor.GOLD)
                            .create()
            );
        }, 20L * expirationTimeSeconds);
    }

    private void createPartyOwnedBy(Player player) {
        final var party = new Party(server, player);
        partyManager.registerParty(party);
    }

    @Subcommand("accept")
    public class AcceptInviteCommand extends BaseCommand {
        private static final String NO_INVITES_MSG = ChatColor.RED + "You don't have any invites!";

        @Default
        public void onAccept(Player caller) {
            if (partyManager.getPartyWith(caller) != null) {
                caller.sendMessage(ChatColor.RED + "You are already in a party!");
                return;
            }

            final var uuid = caller.getUniqueId();

            if (!invites.containsKey(uuid)) {
                caller.sendMessage(NO_INVITES_MSG);
                return;
            }

            final var inviteList = invites.get(uuid);
            final var invite = inviteList.stream()
                    .findAny();

            if (invite.isPresent())
                acceptParty(caller, invite.get());
            else
                caller.sendMessage(NO_INVITES_MSG);
        }

        @Default
        public void onAcceptName(final Player caller, final OnlinePlayer invitePlayer) {
            if (partyManager.getPartyWith(caller) != null) {
                caller.sendMessage(ChatColor.RED + "You are already in a party!");
                return;
            }

            final var uuid = caller.getUniqueId();

            if (!invites.containsKey(uuid)) {
                caller.sendMessage(NO_INVITES_MSG);
                return;
            }

            final var partyOwner = invitePlayer.getPlayer();
            final var inviteList = invites.get(uuid);
            final var invite = inviteList.stream()
                    .filter(party -> party.partyOwner().getUniqueId().equals(partyOwner.getUniqueId()))
                    .findAny();

            if (invite.isPresent())
                acceptParty(caller, invite.get());
            else
                caller.sendMessage(NO_INVITES_MSG);
        }

        private void acceptParty(Player accepter, Party partyToJoin) {
            partyToJoin.addPlayer(accepter);
            partyToJoin.broadcastMessage(
                    new ComponentBuilder("\n")
                            .append(accepter.getDisplayName()).color(ChatColor.YELLOW)
                            .append(" has joined the party!\n").color(ChatColor.GOLD)
                            .create()
            );
        }
    }

    @Subcommand("disband")
    public void onDisband(@NotNull Player caller) {
        // sender has to be a party owner
        if (!checkIfSenderInParty(caller))
            return;

        if (!checkIfSenderPartyOwner(caller))
            return;

        // Grab the party the player is in
        final var party = partyManager.getPartyWith(caller);
        assert party != null;
        assert party.partyOwner().equals(caller);

        party.disband();
        partyManager.unregisterParty(party);
    }

    @Subcommand("list")
    public void onList(@NotNull Player caller) {
        if (!checkIfSenderInParty(caller))
            return;

        final var party = partyManager.getPartyWith(caller);
        assert party != null;

        final var listMessage = new ComponentBuilder("---\n").color(ChatColor.RED)
                .append("Party Owner: ").color(ChatColor.GOLD)
                .append(party.partyOwner().getName()).color(ChatColor.YELLOW)
                .append("\n");

        // Loop through all players in the party and append to
        // the list message
        party.getAllPlayers().forEach(player -> {
            if (player.isOnline())
                listMessage.append(player.getName()).color(ChatColor.YELLOW)
                        .append("\n");
            else
                listMessage.append(player.getName()).color(ChatColor.RED)
                        .append("\n");
        });

        // Send the completed message
        caller.spigot().sendMessage(
                listMessage.append("---").color(ChatColor.RED)
                        .create()
        );
    }

    private boolean checkIfSenderInParty(@NotNull Player sender) {
        final var party = partyManager.getPartyWith(sender);
        if (party == null) {
            sender.spigot().sendMessage(
                    new ComponentBuilder("You need to be in a party to use that command!")
                            .color(ChatColor.RED)
                            .create()
            );

            return false;
        }

        return true;
    }

    private boolean checkIfSenderPartyOwner(@NotNull Player sender) {
        final var party = partyManager.getPartyWith(sender);
        assert party != null;

        if (!party.partyOwner().equals(sender)) {
            sender.spigot().sendMessage(
                    new ComponentBuilder("You need to be a party owner to use that command!")
                            .color(ChatColor.RED)
                            .create()
            );

            return false;
        }

        return true;
    }
}
