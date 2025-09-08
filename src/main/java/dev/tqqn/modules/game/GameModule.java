package dev.tqqn.modules.game;

import com.google.common.collect.ImmutableList;
import dev.tqqn.BingoMain;
import dev.tqqn.modules.AbstractModule;
import dev.tqqn.modules.database.DatabaseModule;
import dev.tqqn.modules.game.framework.GameInstance;
import dev.tqqn.modules.game.framework.listeners.LobbyListeners;
import dev.tqqn.modules.game.framework.listeners.PlayerJoinListener;
import dev.tqqn.modules.game.framework.objects.BingoTask;
import dev.tqqn.modules.game.framework.types.BingoSoloGame;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public final class GameModule extends AbstractModule {

    private final DatabaseModule databaseModule;

    @Getter private GameInstance currentInstance = null;

    private final List<BingoTask> availableTasks = new ArrayList<>();

    public static int GAME_MIN_PLAYERS_TO_START;
    public static int GAME_MAX_PLAYERS;

    public GameModule(BingoMain plugin, DatabaseModule databaseModule) {
        super(plugin, "Game");
        this.databaseModule = databaseModule;
    }

    @Override
    protected void onEnable() {
        GAME_MIN_PLAYERS_TO_START = BingoMain.getInstance().getModuleManager().getModule(DatabaseModule.class).getDefaultConfig().getNeededPlayersToStart();
        GAME_MAX_PLAYERS = BingoMain.getInstance().getModuleManager().getModule(DatabaseModule.class).getDefaultConfig().getMaxPlayers();

        availableTasks.addAll(databaseModule.getBingoTaskConfig().getAllTasks());
        this.currentInstance = new BingoSoloGame(1, this);
        this.currentInstance.start();
        register(new PlayerJoinListener(this));
        register(new LobbyListeners(this));
    }

    public List<BingoTask> getAvailableTasks() {
        return ImmutableList.copyOf(availableTasks);
    }
}
