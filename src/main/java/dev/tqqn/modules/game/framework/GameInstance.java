package dev.tqqn.modules.game.framework;

import dev.tqqn.BingoMain;
import dev.tqqn.modules.game.framework.roles.Roles;
import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class GameInstance {

    private final int id;
    private final GameInstanceTask instanceTask;

    private GameStates currentState = GameStates.LOBBY;

    public GameInstance(int id) {
        this.id = id;
        this.instanceTask = new GameInstanceTask();
    }

    public void start() {
        instanceTask.runTaskTimerAsynchronously(BingoMain.getInstance(), 0L, 20L);
        onStart();
    }

    public void stop() {
        try {
            if (instanceTask.isCancelled()) return;
            instanceTask.cancel();
        } catch (IllegalArgumentException ignored) {}

        onStop();
    }

    public void changeState(GameStates newState) {
        disableState(currentState);
        currentState = newState;
        enableState(currentState);
    }

    public abstract void enableState(GameStates state);
    public abstract void disableState(GameStates state);

    public abstract void addPlayer(UUID uuid);
    public abstract void addPlayer(UUID uuid, Roles role);

    public abstract void removePlayer(UUID uuid);

    public abstract void onStart();
    public abstract void onStop();

    public abstract void onTick();

}
