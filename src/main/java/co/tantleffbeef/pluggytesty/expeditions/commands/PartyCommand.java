package co.tantleffbeef.pluggytesty.expeditions.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import co.tantleffbeef.pluggytesty.expeditions.Party;
import co.tantleffbeef.pluggytesty.expeditions.PartyManager;
import co.tantleffbeef.pluggytesty.misc.TimedRecord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CommandAlias("party|p")
@SuppressWarnings("unused")
public class PartyCommand extends BaseCommand {
    private final static BaseComponent[] NOT_OWNER_ERROR =
            new ComponentBuilder("You are not the owner of the party!")
                    .color(ChatColor.RED)
                    .create();

    private final Server server;
    private final PartyManager partyManager;
    private final Map<UUID, TimedRecord<Party>> invites;

    public PartyCommand(Server server, PartyManager partyManager) {
        this.partyManager = partyManager;
        this.server = server;
        this.invites = new HashMap<>();
    }

    @Subcommand("invite")
    public void onInvite(@NotNull Player caller, @NotNull OnlinePlayer toInvite) {
        final var party = partyManager.getPartyWith(caller);
        final var invitee = toInvite.getPlayer();

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

        // Send message to caller
        caller.spigot().sendMessage(
                new ComponentBuilder("Sent an invite to ").color(ChatColor.GOLD)
                        .append(invitee.getDisplayName()).color(ChatColor.YELLOW)
                        .create());

        // Send invite to invitee
        invites.put(invitee.getUniqueId(), TimedRecord.now(party));

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
                new ComponentBuilder(caller.getDisplayName()).color(ChatColor.YELLOW)
                        .append(" sent you an invite to their party.\n").color(ChatColor.GOLD)
                        .append(clickHereMessage).color(ChatColor.YELLOW)
                        .append(" to join.").color(ChatColor.GOLD)
                        .create()
        );
    }

    private void createPartyOwnedBy(Player player) {
        final var party = new Party(server, player);
        partyManager.registerParty(party);
    }

    @Subcommand("accept")
    public class AcceptInviteCommand extends BaseCommand {
        @Default
        public void onAccept(Player caller) {
            caller.sendMessage("accept invite command lol");
        }

        @CatchUnknown
        public void onAcceptName(Player caller, Player acceptance) {
            caller.sendMessage("trying to accept invite from " + acceptance.getName());
        }
    }

}
