package dev.tqqn.modules.game.framework.states.end;

import dev.tqqn.modules.game.framework.abstraction.GameInstance;
import dev.tqqn.modules.game.framework.GameStates;
import dev.tqqn.modules.game.framework.states.abstraction.AbstractState;
import dev.tqqn.modules.scoreboard.boards.LobbyScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class EndState extends AbstractState {

    public EndState(GameInstance instance) {
        super(instance, GameStates.END, "End", true);
        setTimer(10);
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
