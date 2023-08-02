package co.tantleffbeef.pluggytesty.expeditions.commands;

import co.tantleffbeef.pluggytesty.PluggyTesty;
import co.tantleffbeef.pluggytesty.misc.Debug;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadExpeditionsCommand implements CommandExecutor {
    private final PluggyTesty plugin;

    public ReloadExpeditionsCommand(@NotNull PluggyTesty plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Debug.alwaysInfo(sender, "loading expeditions...");
        plugin.getServer().getScheduler().runTask(plugin, () -> plugin.loadRoomsAndExpeditions(sender));

        return true;
    }
}
