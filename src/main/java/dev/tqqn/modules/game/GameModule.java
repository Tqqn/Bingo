package dev.tqqn.modules.game;

import com.google.common.collect.ImmutableList;
import dev.tqqn.BingoMain;
import dev.tqqn.modules.AbstractModule;
import dev.tqqn.modules.database.DatabaseModule;
import dev.tqqn.modules.database.framework.objects.PlayerModel;
import dev.tqqn.modules.game.commands.BingoCommands;
import dev.tqqn.modules.game.framework.listeners.PlayerJoinListener;
import dev.tqqn.modules.game.framework.listeners.PlayerQuitListener;
import dev.tqqn.modules.game.framework.map.Arena;
import dev.tqqn.modules.game.framework.map.schematic.SchematicProvider;
import dev.tqqn.modules.game.framework.objects.BingoTask;
import dev.tqqn.modules.game.framework.states.GameStateSeries;
import dev.tqqn.modules.game.framework.team.TeamProvider;
import dev.tqqn.modules.game.framework.type.GameType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

public final class GameModule extends AbstractModule {

    private final DatabaseModule databaseModule;

    @Getter private GameStateSeries currentInstance = null;

    private final List<BingoTask> availableTasks = new ArrayList<>();

    private TeamProvider teamProvider;

    @Getter private Arena arena;

    @Getter private final SchematicProvider schematicProvider = new SchematicProvider(this);

    public GameModule(BingoMain plugin, DatabaseModule databaseModule) {
        super(plugin, "Game");
        this.databaseModule = databaseModule;
    }

    @Override
    protected void onEnable() {
        Bukkit.getScheduler().runTask(getPlugin(), () -> {
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
            final PlayerModel playerModel = PlayerModel.from(player);
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
