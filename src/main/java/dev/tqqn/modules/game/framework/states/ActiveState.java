package dev.tqqn.modules.game.framework.states;

import dev.tqqn.modules.game.framework.GameInstance;
import dev.tqqn.modules.game.framework.GameStates;

public final class ActiveState extends AbstractState {

    public ActiveState(GameInstance instance) {
        super(instance, GameStates.ACTIVE, "Active");
    }

    @Override
    public void onEnable() {
        setTimer(1200); // 15 min
    }

    @Override
    public void run() {
        timer--;
    }
}
