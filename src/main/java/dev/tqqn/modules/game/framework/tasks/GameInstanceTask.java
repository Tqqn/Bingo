package dev.tqqn.modules.game.framework.tasks;

import dev.tqqn.modules.game.framework.GameInstance;
import dev.tqqn.modules.game.framework.GameStates;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class GameInstanceTask extends BukkitRunnable {

    private final GameInstance gameInstance;

    @Getter private int seconds = 0;

    @Override
    public void run() {
        seconds++;

        if (gameInstance.isThereAWinner()) {
            gameInstance.changeState(GameStates.END);
        }

        if (gameInstance.canStart()) {
            gameInstance.changeState(GameStates.ACTIVE);
        }

        gameInstance.onTick();
    }

}
