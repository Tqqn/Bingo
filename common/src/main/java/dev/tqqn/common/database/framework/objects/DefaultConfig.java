package dev.tqqn.common.database.framework.objects;

import dev.tqqn.common.GamePlugin;
import dev.tqqn.common.database.AbstractDatabaseModule;
import dev.tqqn.common.database.framework.config.DatabaseCredentials;

public final class DefaultConfig {

    private static DefaultConfig INSTANCE;

    private final GamePlugin<?> bingoMain;

    private DefaultConfig(AbstractDatabaseModule databaseModule) {
        this.bingoMain = databaseModule.getModuleManager().getPlugin();
        bingoMain.getPlugin().saveDefaultConfig();
    }

    public static DefaultConfig getInstance(AbstractDatabaseModule databaseModule) {
        if (INSTANCE == null) {
            INSTANCE = new DefaultConfig(databaseModule);
        }
        return INSTANCE;
    }

    public String getDBHost() {
        return bingoMain.getPlugin().getConfig().getString("database.host");
    }

    public String getDBDataBase() {
        return bingoMain.getPlugin().getConfig().getString("database.database");
    }

    public String getDBUserName() {
        final String value = bingoMain.getPlugin().getConfig().getString("database.username");
        return value == null || value.isBlank() || value.equalsIgnoreCase("change_me") ? null : value;
    }

    public String getDBPassword() {
        final String value = bingoMain.getPlugin().getConfig().getString("database.password");
        return value == null || value.isBlank() ? null : value;
    }

    public int getPort() {
        final int value = bingoMain.getPlugin().getConfig().getInt("database.port");
        return value <= 0 ? 27017 : value;
    }

    public DatabaseCredentials getDatabaseCredentials() {
        return new DatabaseCredentials(getDBHost(), getPort(), getDBDataBase(),  getDBUserName(), getDBPassword());
    }

    public int getNeededPlayersToStart() {
        return bingoMain.getPlugin().getConfig().getInt("game.needed_players");
    }

    public int getMaxPlayers() {
        return bingoMain.getPlugin().getConfig().getInt("game.max_players");
    }
}
