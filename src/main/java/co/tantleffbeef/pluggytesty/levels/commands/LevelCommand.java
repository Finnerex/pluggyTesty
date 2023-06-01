package co.tantleffbeef.pluggytesty.levels.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import co.tantleffbeef.pluggytesty.plugger.OfflineGoober;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@CommandAlias("level|lvl")
public class LevelCommand extends BaseCommand {
    @Subcommand("get")
    @CommandCompletion("@players")
    public void onGet(@NotNull CommandSender sender, @NotNull OfflineGoober toCheck) {
        sender.sendMessage(toCheck.asOfflinePlayer().getName() + "'s level is " + ChatColor.GOLD + toCheck.getLevel());
    }

    @Subcommand("set")
    @CommandCompletion("@players")
    public void onSet(@NotNull CommandSender sender, @NotNull OfflineGoober toModify, int level) {
        toModify.setLevel(level);
        sender.sendMessage(toModify.asOfflinePlayer().getName() + "'s level has been set to " + level);
    }

    @Subcommand("add")
    @CommandCompletion("@players")
    public void onAdd(@NotNull CommandSender sender, @NotNull OfflineGoober toLevelUp) {
        onAdd(sender, toLevelUp, 1);
    }

    @Subcommand("add")
    @CommandCompletion("@players")
    public void onAdd(@NotNull CommandSender sender, @NotNull OfflineGoober toLevelUp, int amount) {
        toLevelUp.addLevels(amount);
        sender.sendMessage(toLevelUp.asOfflinePlayer().getName() + " has levelled up to level " +
                toLevelUp.getLevel() + "!");
    }
}
