package co.tantleffbeef.pluggytesty.levels;

import co.tantleffbeef.pluggytesty.misc.Debug;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public class YmlLevelStore implements LevelStore {
    private final YamlConfiguration config;
    private final Path configPath;
    private final ConfigurationSection levels;
    private final int defaultLevel;

    public YmlLevelStore(@NotNull Path configPath, int defaultLevel) {
        this.defaultLevel = defaultLevel;
        this.configPath = configPath;

        this.config = YamlConfiguration.loadConfiguration(configPath.toFile());
        this.levels = createLevelsSection(config);
    }

    private static ConfigurationSection createLevelsSection(YamlConfiguration config) {
        if (config.isConfigurationSection("levels"))
            return config.getConfigurationSection("levels");

        return config.createSection("levels");
    }

    @Override
    public int retrieveLevel(@NotNull UUID player) {
        return levels.getInt(player.toString(), defaultLevel);
    }

    @Override
    public void storeLevel(@NotNull UUID player, int level) {
        levels.set(player.toString(), level);

        try {
            config.save(configPath.toFile());
        } catch (IOException e) {
            Debug.alwaysError("[LevelStore] Unable to save " + configPath.getFileName() + "!");
        }
    }
}
