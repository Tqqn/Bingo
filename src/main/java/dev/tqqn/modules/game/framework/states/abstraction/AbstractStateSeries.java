package dev.tqqn.modules.game.framework.states.abstraction;

import dev.tqqn.modules.game.GameModule;
import dev.tqqn.modules.game.framework.abstraction.GameInstance;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public abstract class AbstractStateSeries extends GameInstance {

    private final List<AbstractState> states = new ArrayList<>();

    private int currentStatePosition = 0;
    private WeakReference<AbstractState> currentState;

    public AbstractStateSeries(int id, GameModule gameModule) {
        super(id, gameModule);
    }

    public void enable() {
        if (states.isEmpty()) {
            getGameModule().getLogger().log(Level.SEVERE, "GameState-Series not enabled. No valid states.");
            return;
        }
        this.currentState = new WeakReference<>(states.get(0));
        this.runTaskTimer(getGameModule().getPlugin(), 0L, 20L);
        onEnable();
        this.currentState.get().enable();
    }

    public void disable() {
        cancel();
        onDisable();
    }

    @Override
    public void run() {
        final AbstractState possibleCurrentState = currentState.get();
        if (possibleCurrentState == null) {
            disable();
            return;
        }

        possibleCurrentState.tick();
    }

    @Override
    public WeakReference<AbstractState> getCurrentState() {
        return currentState;
    }

    public void registerStates(List<AbstractState> states) {
        this.states.addAll(states);
    }

    public void registerState(AbstractState[] states) {
        this.states.addAll(Arrays.asList(states));
    }

    public abstract void onEnable();
    public abstract void onDisable();

    public void previousState() {
        if (currentStatePosition <= 0) return;

        final AbstractState currentState = getState(currentStatePosition);
        if (currentState == null) return;

        final AbstractState newState = states.get(currentStatePosition-1);

        if (newState == null) return;

        currentState.disable();
        this.currentState = new WeakReference<>(newState);
        currentStatePosition = currentStatePosition - 1;
        this.currentState.get().enable();
    }

    public AbstractState getState(int position) {
        return states.get(position);
    }

    public void freeze(boolean value) {
        final AbstractState state = currentState.get();
        if (state == null) return;

        state.freeze = value;
        onFreeze();
    }

    public void nextState() {
        if (states.size() < currentStatePosition) return;

        final AbstractState oldState = currentState.get();
        if (oldState == null) return;

        final AbstractState nextState = states.get(currentStatePosition+1);

        if (nextState == null) return;

        oldState.disable();
        this.currentState = new WeakReference<>(nextState);
        currentStatePosition = currentStatePosition + 1;
        this.currentState.get().enable();
    }

    public abstract void onFreeze();

    public AbstractState getNextState() {
        AbstractState state;

        try {
            state = states.get(currentStatePosition+1);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }

        return state;
    }

    public AbstractState getPreviousState() {
        AbstractState state;

        try {
            state = states.get(currentStatePosition-1);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }

        return state;
    }
}
