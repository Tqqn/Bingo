package dev.tqqn.modules.game.framework.states.active;

import dev.tqqn.modules.game.framework.abstraction.GameInstance;
import dev.tqqn.modules.game.framework.GameStates;
import dev.tqqn.modules.game.framework.states.abstraction.AbstractState;
import dev.tqqn.modules.scoreboard.boards.ActiveScoreboard;
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
    public void onTick() {
        timer--;
    }

    @Override
    public void setScoreboard(Player player) {
        applyScoreboard(new ActiveScoreboard(player, getGameInstance()), player);
    }

    @Override
    public void onDisable() {
        for (Player player : getGameInstance().getInGamePlayers().keySet()) {
            removeScoreboard(ActiveScoreboard.class, player);
        }
    }
}
