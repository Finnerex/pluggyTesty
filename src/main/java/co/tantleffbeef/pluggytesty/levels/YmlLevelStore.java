package co.tantleffbeef.pluggytesty.levels;

import co.tantleffbeef.pluggytesty.misc.Debug;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public class YmlLevelStore implements LevelStore {
    private final static String SECTION_PATH = "levels";

    private final YamlConfiguration config;
    private final Path configPath;
    private final ConfigurationSection levels;
    private final int defaultLevel;
    private final Scoreboard levelBoard;
    private final Server server;

    public YmlLevelStore(@NotNull Path configPath, int defaultLevel, Server server) {
        this.defaultLevel = defaultLevel;
        this.configPath = configPath;

        this.config = YamlConfiguration.loadConfiguration(configPath.toFile());
        this.levels = createLevelsSection(config);
        this.server = server;

        // level scoreboard
        levelBoard = server.getScoreboardManager().getNewScoreboard();
        levelBoard.registerNewObjective("gooberLevel", Criteria.create("gooberLevel"), "level")
                .setDisplaySlot(DisplaySlot.PLAYER_LIST);

    }

    private static ConfigurationSection createLevelsSection(YamlConfiguration config) {
        if (config.isConfigurationSection(SECTION_PATH))
            return config.getConfigurationSection(SECTION_PATH);

        return config.createSection(SECTION_PATH);
    }

    @Override
    public int retrieveLevel(@NotNull UUID player) {
        return levels.getInt(player.toString(), defaultLevel);
    }

    @Override
    public void storeLevel(@NotNull UUID player, int level) {
        levels.set(player.toString(), level);

        // update the scoreboard
        Player playerPlayer = server.getPlayer(player);

        // set the scoreboard level
        levelBoard.getObjective("gooberLevel")
                .getScore(playerPlayer.getName())
                .setScore(level);

        playerPlayer.setScoreboard(levelBoard);

        try {
            config.save(configPath.toFile());
        } catch (IOException e) {
            Debug.alwaysError("[LevelStore] Unable to save " + configPath.getFileName() + "!");
        }
    }
}
