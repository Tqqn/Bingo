package dev.tqqn.modules.game.framework.abstraction;

import dev.tqqn.modules.game.GameModule;
import dev.tqqn.modules.game.framework.roles.Roles;
import dev.tqqn.modules.game.framework.states.GameStateSeries;
import dev.tqqn.modules.game.framework.states.abstraction.AbstractStateSeries;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class GameInstance {

    private final int id;
    private final GameModule gameModule;

    private final AbstractStateSeries gameStateSeries;

    private final Map<Player, Roles> inGamePlayers = new HashMap<>();

    public GameInstance(int id, GameModule gameModule) {
        this.id = id;
        this.gameModule = gameModule;
        this.gameStateSeries = new GameStateSeries(this);
    }

    public void start() {
        gameStateSeries.enable();
    }

    public void stop() {
        gameStateSeries.disable();
    }

    public void addPlayer(Player player, Roles role) {
        inGamePlayers.put(player, role);
    }

    public int getInGameAlivePlayerCount() {
        return inGamePlayers.values().stream().filter(role -> role == Roles.ALIVE).toList().size();
    }

    public abstract boolean canStart();
}
