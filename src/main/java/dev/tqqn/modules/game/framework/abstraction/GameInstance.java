package dev.tqqn.modules.game.framework.abstraction;

import dev.tqqn.modules.database.framework.events.PlayerModelJoinEvent;
import dev.tqqn.modules.database.framework.events.PlayerModelPreJoinEvent;
import dev.tqqn.modules.database.framework.objects.PlayerModel;
import dev.tqqn.modules.game.GameModule;
import dev.tqqn.modules.game.framework.roles.Roles;
import dev.tqqn.modules.game.framework.states.abstraction.AbstractState;
import dev.tqqn.modules.game.framework.type.GameType;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public abstract class GameInstance extends BukkitRunnable {

    @Getter private final int id;
    @Getter private final GameModule gameModule;
    @Getter private final Map<Player, Roles> inGamePlayers = new HashMap<>();

    @Getter private final GameSettings gameSettings;

    public GameInstance(int id, GameModule gameModule) {
        this.id = id;
        this.gameModule = gameModule;
        this.gameSettings = new GameSettings();
    }

    public abstract void start();

    public abstract void stop();

    public void addPlayer(Player player, Roles role) {
        inGamePlayers.put(player, role);
    }

    public int getInGameAlivePlayerCount() {
        return inGamePlayers.values().stream().filter(role -> role == Roles.ALIVE).toList().size();
    }

    public abstract WeakReference<AbstractState> getCurrentState();

    public abstract boolean canStart();

    public void onPlayerJoin(PlayerModel playerModel, PlayerModelJoinEvent event) {
        getCurrentState().get().onPlayerJoin(playerModel, event);
    }

    public void onPlayerPreJoin(PlayerModel playerModel, PlayerModelPreJoinEvent event) {
        getCurrentState().get().onPlayerPreJoin(playerModel, event);
    }

    public void setGameType(GameType gameType) {
        this.gameSettings.setGameType(gameType);
    }

    public void setMinGamePlayersToStart(int minPlayers) {
        this.gameSettings.setMinGamePlayersToStart(minPlayers);
    }

    public void setMaxGamePlayers(int maxPlayers) {
        this.gameSettings.setMaxGamePlayers(maxPlayers);
    }

    public void setPlayersPerTeam(int playersPerTeam) {
        this.gameSettings.setPlayersPerTeam(playersPerTeam);
    }
}
