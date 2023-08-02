package co.tantleffbeef.pluggytesty.misc;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

public final class Debug {
    private static ConsoleCommandSender sender;
    private static boolean debugMessagesEnabled;

    private Debug() {}

    public static void setConsoleSender(@NotNull ConsoleCommandSender consoleCommandSender) {
        sender = consoleCommandSender;
    }

    public static void setDebugMessagesEnabled(boolean enabled) {
        debugMessagesEnabled = enabled;
    }

    public static void log(@NotNull String toLog) {
        assert sender != null;

        if (!debugMessagesEnabled)
            return;

        sender.sendMessage("[pluggyTesty] (DBG) " + toLog);
    }

    public static void log(@NotNull CommandSender logger, @NotNull String toLog) {
        if (!debugMessagesEnabled)
            return;

        logger.sendMessage("[pluggyTesty] (DBG) " + toLog);
    }

    public static void alwaysLog(@NotNull String toLog) {
        assert sender != null;

        sender.sendMessage("[pluggyTesty] " + toLog);
    }

    public static void alwaysLog(@NotNull CommandSender logger, @NotNull String toLog) {
        assert sender != null;

        logger.sendMessage("[pluggyTesty] " + toLog);
    }

    public static void info(@NotNull String infoMessage) {
        if (!debugMessagesEnabled)
            return;

        log(ChatColor.BOLD + "info" + ChatColor.RESET + ": " + infoMessage);
    }

    public static void info(@NotNull CommandSender logger, @NotNull String infoMessage) {
        if (!debugMessagesEnabled)
            return;

        log(logger, ChatColor.BOLD + "info" + ChatColor.RESET + ": " + infoMessage);
    }

    public static void alwaysInfo(@NotNull String infoMessage) {
        alwaysLog(ChatColor.BOLD + "info" + ChatColor.RESET + ": " + infoMessage);
    }

    public static void alwaysInfo(@NotNull CommandSender logger, @NotNull String infoMessage) {
        alwaysLog(logger, ChatColor.BOLD + "info" + ChatColor.RESET + ": " + infoMessage);
    }

    public static void success(@NotNull String successMessage) {
        if (!debugMessagesEnabled)
            return;

        log(ChatColor.GREEN.toString() + ChatColor.BOLD + "success" + ChatColor.RESET + ": " + successMessage);
    }

    public static void success(@NotNull CommandSender logger, @NotNull String successMessage) {
        if (!debugMessagesEnabled)
            return;

        log(logger,ChatColor.GREEN.toString() + ChatColor.BOLD + "success" + ChatColor.RESET + ": " + successMessage);
    }

    public static void alwaysSuccess(@NotNull String successMessage) {
        alwaysLog(ChatColor.GREEN.toString() + ChatColor.BOLD + "success" + ChatColor.RESET + ": " + successMessage);
    }

    public static void alwaysSuccess(@NotNull CommandSender logger, @NotNull String successMessage) {
        alwaysLog(logger, ChatColor.GREEN.toString() + ChatColor.BOLD + "success" + ChatColor.RESET + ": " + successMessage);
    }

    public static void warn(@NotNull String warningMessage) {
        if (!debugMessagesEnabled)
            return;

        log(ChatColor.YELLOW.toString() + ChatColor.BOLD + "warning" + ChatColor.RESET + ": " + warningMessage);
    }

    public static void warn(@NotNull CommandSender logger, @NotNull String warningMessage) {
        if (!debugMessagesEnabled)
            return;

        log(logger, ChatColor.YELLOW.toString() + ChatColor.BOLD + "warning" + ChatColor.RESET + ": " + warningMessage);
    }

    public static void alwaysWarn(@NotNull String warningMessage) {
        alwaysLog(ChatColor.YELLOW.toString() + ChatColor.BOLD + "warning" + ChatColor.RESET + ": " + warningMessage);
    }

    public static void alwaysWarn(@NotNull CommandSender logger, @NotNull String warningMessage) {
        alwaysLog(logger, ChatColor.YELLOW.toString() + ChatColor.BOLD + "warning" + ChatColor.RESET + ": " + warningMessage);
    }

    public static void error(@NotNull String error) {
        if (!debugMessagesEnabled)
            return;

        log(ChatColor.RED.toString() + ChatColor.BOLD + "error" + ChatColor.RESET + ": " + error);
    }

    public static void error(@NotNull CommandSender logger, @NotNull String error) {
        if (!debugMessagesEnabled)
            return;

        log(logger, ChatColor.RED.toString() + ChatColor.BOLD + "error" + ChatColor.RESET + ": " + error);
    }

    public static void alwaysError(@NotNull String error) {
        alwaysLog(ChatColor.RED.toString() + ChatColor.BOLD + "error" + ChatColor.RESET + ": " + error);
    }

    public static void alwaysError(@NotNull CommandSender logger, @NotNull String error) {
        alwaysLog(logger, ChatColor.RED.toString() + ChatColor.BOLD + "error" + ChatColor.RESET + ": " + error);
    }
}
