package co.tantleffbeef.pluggytesty.levels.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import co.tantleffbeef.pluggytesty.levels.LevelController;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@CommandAlias("level|lvl")
public class LevelCommand extends BaseCommand {
    private final LevelController levelController;

    public LevelCommand(@NotNull LevelController levelController) {
        this.levelController = levelController;
    }

    @Subcommand("get")
    @CommandCompletion("@players")
    public void onGet(@NotNull CommandSender sender, @NotNull OfflinePlayer toCheck) {
        final int playerLevel = levelController.getPlayerLevel(toCheck);

        sender.sendMessage(toCheck.getName() + "'s level is " + ChatColor.GOLD + playerLevel);
    }

    @Subcommand("set")
    @CommandCompletion("@players")
    public void onSet(@NotNull CommandSender sender, @NotNull OfflinePlayer toModify, int level) {
        levelController.setLevel(toModify, level);
        sender.sendMessage(toModify.getName() + "'s level has been set to " + level);
    }

    @Subcommand("up")
    @CommandCompletion("@players")
    public void onLevelUp(@NotNull CommandSender sender, @NotNull OfflinePlayer toLevelUp) {
        levelController.levelUp(toLevelUp);
        sender.sendMessage(toLevelUp.getName() + " has levelled up to level " + levelController.getPlayerLevel(toLevelUp) + "!");
    }
}
