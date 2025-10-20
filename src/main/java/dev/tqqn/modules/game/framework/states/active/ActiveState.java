package dev.tqqn.modules.game.framework.states.active;

import dev.tqqn.modules.game.framework.abstraction.GameInstance;
import dev.tqqn.modules.game.framework.GameStates;
import dev.tqqn.modules.game.framework.states.abstraction.AbstractState;
import org.bukkit.entity.Player;

public final class ActiveState extends AbstractState {

    public ActiveState(GameInstance instance) {
        super(instance, GameStates.ACTIVE, "Active");
    }

    @Override
    public void onEnable() {
        setTimer(1200); // 15 min
    }

    @Override
    public void applyScoreboard(Player player) {

    }

    @Override
    public void onTick() {
        timer--;
    }
}
