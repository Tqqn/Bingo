package dev.tqqn.game.modules.game;

import com.google.common.collect.ImmutableList;
import dev.tqqn.common.modular.AbstractModule;
import dev.tqqn.game.BingoMain;
import dev.tqqn.game.BingoModuleManager;
import dev.tqqn.game.modules.database.GameDatabaseModule;
import dev.tqqn.game.modules.database.player.BingoPlayerModel;
import dev.tqqn.game.modules.game.commands.BingoCommands;
import dev.tqqn.game.modules.game.framework.listeners.PlayerJoinListener;
import dev.tqqn.game.modules.game.framework.listeners.PlayerQuitListener;
import dev.tqqn.game.modules.game.framework.map.Arena;
import dev.tqqn.game.modules.game.framework.map.schematic.SchematicProvider;
import dev.tqqn.game.modules.game.framework.objects.BingoTask;
import dev.tqqn.game.modules.game.framework.states.GameStateSeries;
import dev.tqqn.game.modules.game.framework.team.TeamProvider;
import dev.tqqn.game.modules.game.framework.type.GameType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

public final class GameModule extends AbstractModule<BingoModuleManager> {

    private final GameDatabaseModule databaseModule;

    @Getter private GameStateSeries currentInstance = null;

    private final List<BingoTask> availableTasks = new ArrayList<>();

    private TeamProvider teamProvider;

    @Getter private Arena arena;

    @Getter private final SchematicProvider schematicProvider = new SchematicProvider(this);

    public GameModule(BingoModuleManager moduleManager, GameDatabaseModule databaseModule) {
        super(moduleManager, "Game");
        this.databaseModule = databaseModule;
    }

    @Override
    protected void onEnable() {
        Bukkit.getScheduler().runTask(getModuleManager().getPlugin().getPlugin(), () -> {
            arena = new Arena(this, "bingo");
            arena.setUp();
        });

        register(new PlayerJoinListener(this));
        register(new PlayerQuitListener(this));
        register(new BingoCommands(this));

        availableTasks.addAll(databaseModule.getBingoTaskConfig().getAllTasks());

        this.currentInstance = new GameStateSeries(this, provideNewInstanceId());
        registerEnvironmentVariables();
        this.teamProvider = new TeamProvider(4, this.currentInstance.getGameSettings().getPlayersPerTeam());
        GameType gameType = (this.currentInstance.getGameSettings().getPlayersPerTeam() > 1 ? GameType.TEAM : GameType.SOLO);
        this.currentInstance.setGameType(gameType);

        this.currentInstance.setMaxGamePlayers(this.currentInstance.getGameSettings().getPlayersPerTeam() * 4);
        this.currentInstance.start();
    }

    public List<BingoTask> getAvailableTasks() {
        return ImmutableList.copyOf(availableTasks);
    }

    public void putPlayersInTeams() {
        for (Player player : currentInstance.getInGamePlayers().keySet()) {
            final BingoPlayerModel playerModel = BingoPlayerModel.from(player);
            if (playerModel == null) {
                continue;
            }

            teamProvider.assignTeam(playerModel);
        }
    }

    private int getIntEnv(String envVarName) {
        final String envString = System.getenv(envVarName);

        int value = -1;

        try {
            value = Integer.parseInt(envString);
        } catch (NumberFormatException e) {
            getLogger().log(Level.SEVERE, "Invalid int value for " + envVarName + " environment variable: " + envString);
        }

        return value;
    }

    private void registerEnvironmentVariables() {
        final int playersPerTeam = getIntEnv("PLAYERS_PER_TEAM");
        if (playersPerTeam != -1) currentInstance.setPlayersPerTeam(playersPerTeam);

        final int minGamePlayersToStart = getIntEnv("MIN_GAME_PLAYERS_TO_START");
        if (minGamePlayersToStart != -1) currentInstance.setMinGamePlayersToStart(minGamePlayersToStart);
    }

    private int provideNewInstanceId() {
        return ThreadLocalRandom.current().nextInt(1, 1000);
    }
}
