package dev.tqqn.game.modules.game.framework.states.end;

import dev.tqqn.game.modules.game.framework.GameStates;
import dev.tqqn.game.modules.game.framework.states.abstraction.AbstractState;
import dev.tqqn.game.modules.game.framework.states.abstraction.AbstractStateSeries;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class EndState extends AbstractState {

    public EndState(AbstractStateSeries instance) {
        super(instance, GameStates.END, "End", 10, true);
    }

    @Override
    public void onTick() {

    }

    @Override
    public void onTimerEnd() {
        Bukkit.getServer().shutdown();
    }

    @Override
    public void setScoreboard(Player player) {

    }
}
