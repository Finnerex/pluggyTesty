package co.tantleffbeef.pluggytesty.expeditions.commands;

import co.tantleffbeef.pluggytesty.expeditions.ExpeditionBuilder;
import co.tantleffbeef.pluggytesty.expeditions.loading.ExpeditionInformation;
import co.tantleffbeef.pluggytesty.expeditions.parties.Party;
import co.tantleffbeef.pluggytesty.goober.Goober;
import co.tantleffbeef.pluggytesty.goober.GooberStateController;
import co.tantleffbeef.pluggytesty.misc.Debug;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class RunExpeditionCommand implements CommandExecutor, TabCompleter {
    private final Map<String, ExpeditionInformation> expeditionIds;
    private final Server server;
    private final BukkitScheduler scheduler;
    private final ExpeditionBuilder builder;
    private final Plugin plugin;
    private final GooberStateController gooberWrapper;

    public RunExpeditionCommand(@NotNull Map<String, ExpeditionInformation> expeditionIds,
                                @NotNull Server server,
                                @NotNull BukkitScheduler scheduler,
                                @NotNull ExpeditionBuilder builder,
                                @NotNull Plugin plugin,
                                @NotNull GooberStateController gooberWrapper) {
        this.expeditionIds = expeditionIds;
        this.server = server;
        this.scheduler = scheduler;
        this.builder = builder;
        this.plugin = plugin;
        this.gooberWrapper = gooberWrapper;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        String expeditionId;
        Player player;
        Goober goober;
        Party party;
        ExpeditionInformation expeditionInfo;

        if (strings.length < 1 || !(commandSender instanceof Player) && strings.length < 2) {
            commandSender.sendMessage(ChatColor.RED + "Not enough arguments!");
            return false;
        }

        if (strings.length > 1) {
            player = server.getPlayer(strings[0]);
        } else {
            player = (Player) commandSender;
        }

        if (player == null) {
            commandSender.sendMessage(ChatColor.RED + strings[0] + " is not a player!");
            return false;
        }

        goober = gooberWrapper.wrapPlayer(player);
        party = goober.getPartyOrCreate();

        if (strings.length > 1) {
            expeditionId = strings[1];
        } else {
            expeditionId = strings[0];
        }

        if (!expeditionIds.containsKey(expeditionId)) {
            commandSender.sendMessage(ChatColor.RED + expeditionId + " is not a valid expedition id!");
            return false;
        }

        expeditionInfo = expeditionIds.get(expeditionId);
        assert expeditionInfo != null;

        builder.buildExpedition(expeditionInfo)
                .whenComplete((r, e) -> {
                    if (e != null)
                        Debug.alwaysError(e.toString());
                })
                .thenAccept(expedition -> scheduler.runTask(plugin, () -> expedition.start(party)));

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
