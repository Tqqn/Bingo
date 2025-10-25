package dev.tqqn.modules.database.framework.objects;

import dev.tqqn.BingoMain;
import dev.tqqn.modules.database.DatabaseModule;

public final class DefaultConfig {

    private static DefaultConfig INSTANCE;

    private final BingoMain bingoMain;

    private DefaultConfig(DatabaseModule databaseModule) {
        this.bingoMain = databaseModule.getPlugin();
        bingoMain.saveDefaultConfig();
    }

    public static DefaultConfig getInstance(DatabaseModule databaseModule) {
        if (INSTANCE == null) {
            INSTANCE = new DefaultConfig(databaseModule);
        }
        return INSTANCE;
    }

    public String getDBHost() {
        return bingoMain.getConfig().getString("database.host");
    }

    public String getDBDataBase() {
        return bingoMain.getConfig().getString("database.database");
    }

    public String getDBUserName() {
        final String value = bingoMain.getConfig().getString("database.username");
        return (value == null || value.isBlank() || value.isEmpty() ? null : value);
    }

    public String getDBPassword() {
        final String value = bingoMain.getConfig().getString("database.password");
        return (value == null || value.isBlank() || value.isEmpty() ? null : value);
    }

    public int getNeededPlayersToStart() {
        return bingoMain.getConfig().getInt("game.needed_players");
    }

    public int getMaxPlayers() {
        return bingoMain.getConfig().getInt("game.max_players");
    }
}
