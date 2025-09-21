package dev.tqqn.modules.game.framework.states;

import dev.tqqn.modules.game.framework.GameInstance;
import dev.tqqn.modules.game.framework.GameStates;
import org.bukkit.entity.Player;

public final class EndState extends AbstractState {

    public EndState(GameInstance instance) {
        super(instance, GameStates.END, "End");
    }

    @Override
    public void applyScoreboard(Player player) {

    }

    @Override
    public void run() {

    }
}
