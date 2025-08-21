package dev.tqqn.modules.database.framework.objects;

import dev.tqqn.modules.database.DatabaseModule;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public abstract class AbstractConfig {

    @Getter
    private final String configName;
    @Getter private FileConfiguration configuration;
    @Getter private final DatabaseModule databaseModule;

    private File file;

    public AbstractConfig(String configName, DatabaseModule dataModule) {
        this.configName = configName;
        this.databaseModule = dataModule;
        loadConfig();
    }

    private void loadConfig() {
        file = new File(databaseModule.getPlugin().getDataFolder(), configName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            databaseModule.getPlugin().saveResource(configName, false);
        }

        configuration = new YamlConfiguration();

        try {
            configuration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            databaseModule.getLogger().log(Level.SEVERE, e.getMessage());
        }
    }

    public void save() {
        Bukkit.getScheduler().runTaskAsynchronously(databaseModule.getPlugin(), () -> {
            try {
                configuration.save(file);
            } catch (IOException e) {
                databaseModule.getLogger().log(Level.SEVERE, e.getMessage());
            }
        });
    }

    public void saveValue(String path, Object value, boolean async, boolean save) {
        if (async) {
            Bukkit.getScheduler().runTaskAsynchronously(databaseModule.getPlugin(), () -> getConfiguration().set(path, value));
        }
        if (save) this.save();
    }

    public String getValue(String path, boolean async) {
        if (async) {
            final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> (String) configuration.get(path));
            return future.join();
        } else {
            return (String) configuration.get(path);
        }
    }

    public int getInt(String path, boolean async) {
        try {
            return Integer.parseInt(getValue(path, async));
        } catch (NumberFormatException e) {
            databaseModule.getLogger().log(Level.SEVERE, "Could not return a integer from config. -> \n" + e.getMessage());
            return 0;
        }
    }
}
