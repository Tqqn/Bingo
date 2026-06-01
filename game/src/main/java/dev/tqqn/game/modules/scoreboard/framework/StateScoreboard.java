package dev.tqqn.game.modules.scoreboard.framework;

import dev.tqqn.game.modules.game.framework.states.abstraction.AbstractState;
import org.bukkit.entity.Player;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 01/06/2026
 */

public abstract class StateScoreboard<S extends AbstractState> extends SingleScoreboard {

    protected S state;

    public StateScoreboard(Player player, S state) {
        super(player);
        this.state = state;
    }

    protected S getState() {
        return state;
    }
}
