package dev.tqqn.modules.game;

import com.google.common.collect.ImmutableList;
import dev.tqqn.BingoMain;
import dev.tqqn.modules.AbstractModule;
import dev.tqqn.modules.database.DatabaseModule;
import dev.tqqn.modules.database.framework.objects.PlayerModel;
import dev.tqqn.modules.game.commands.BingoCommands;
import dev.tqqn.modules.game.framework.listeners.PlayerJoinListener;
import dev.tqqn.modules.game.framework.objects.BingoTask;
import dev.tqqn.modules.game.framework.states.GameStateSeries;
import dev.tqqn.modules.game.framework.team.TeamProvider;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public final class GameModule extends AbstractModule {

    private final DatabaseModule databaseModule;

    @Getter private GameStateSeries currentInstance = null;

    private final List<BingoTask> availableTasks = new ArrayList<>();

    private final TeamProvider teamProvider;

    private World gameWorld;

    @Getter private boolean readyToJoin = false;

    public static int GAME_MIN_PLAYERS_TO_START;
    public static int GAME_MAX_PLAYERS;

    public GameModule(BingoMain plugin, DatabaseModule databaseModule) {
        super(plugin, "Game");
        this.databaseModule = databaseModule;
        this.teamProvider = new TeamProvider(4);
    }

    @Override
    protected void onEnable() {
        Bukkit.getScheduler().runTask(getPlugin(), () -> {
            final CompletableFuture<World> future = createNewWorld();
            gameWorld = future.join();
            future.whenComplete((world, throwable) -> {
                createWorldBorder(gameWorld, 10000);
                readyToJoin = true;
            });
            System.out.println("World is ready: " + readyToJoin);
        });

        GAME_MIN_PLAYERS_TO_START = BingoMain.getInstance().getModuleManager().getModule(DatabaseModule.class).getDefaultConfig().getNeededPlayersToStart();
        GAME_MAX_PLAYERS = BingoMain.getInstance().getModuleManager().getModule(DatabaseModule.class).getDefaultConfig().getMaxPlayers();

        availableTasks.addAll(databaseModule.getBingoTaskConfig().getAllTasks());
        this.currentInstance = new GameStateSeries(this, provideNewInstanceId());
        this.currentInstance.start();
        register(new PlayerJoinListener(this));
        register(new BingoCommands(this));
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

    private int provideNewInstanceId() {
        return ThreadLocalRandom.current().nextInt(1, 1000);
    }

    public void createWorldBorder(World world, int size) {
        world.getWorldBorder().setCenter(0, 0);
        world.getWorldBorder().setSize(size);
        world.getWorldBorder().setDamageAmount(2);
    }

    public CompletableFuture<World> createNewWorld() {
        final World world = Bukkit.createWorld(new WorldCreator("bingo_world"));
        if (world == null) {
            throw new RuntimeException("Failed to create world 'bingo_world'");
        }

        return CompletableFuture.supplyAsync(() -> world);
    }
}
