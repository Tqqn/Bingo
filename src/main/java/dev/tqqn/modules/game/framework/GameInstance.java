package dev.tqqn.modules.game.framework;

import dev.tqqn.BingoMain;
import dev.tqqn.modules.game.GameModule;
import dev.tqqn.modules.game.framework.roles.Roles;
import dev.tqqn.modules.game.framework.states.AbstractState;
import dev.tqqn.modules.game.framework.tasks.GameInstanceTask;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public abstract class GameInstance {

    private final int id;
    private final GameModule gameModule;
    private final GameInstanceTask activeGameInstanceTask;

    private AbstractState currentState = null;

    public GameInstance(int id, GameModule gameModule) {
        this.id = id;
        this.gameModule = gameModule;
        this.activeGameInstanceTask = new GameInstanceTask(this);
    }

    public void start() {
        activeGameInstanceTask.runTaskTimerAsynchronously(BingoMain.getInstance(), 0L, 20L);
        onStart();
    }

    public void stop() {
        try {
            if (activeGameInstanceTask.isCancelled()) return;
            activeGameInstanceTask.cancel();
        } catch (IllegalArgumentException ignored) {}

        onStop();
    }

    protected void setState(AbstractState state) {
        if (state == null) return;
        if (currentState != null) currentState.disable();
        this.currentState = state;
    }

    public abstract void changeState(GameStates gameStates);

    public abstract void addPlayer(UUID uuid, Roles role);

    public abstract void removePlayer(UUID uuid);

    public abstract void onStart();
    public abstract void onStop();

    public abstract void onTick();

    public abstract boolean isThereAWinner();

    public abstract boolean canStart();

    public abstract int getPlayerCount();
}
