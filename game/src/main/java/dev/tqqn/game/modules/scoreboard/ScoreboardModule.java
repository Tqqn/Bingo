package dev.tqqn.game.modules.scoreboard;

import dev.tqqn.common.modular.AbstractModule;
import dev.tqqn.game.BingoMain;
import dev.tqqn.game.BingoModuleManager;
import dev.tqqn.game.modules.scoreboard.task.ScoreboardUpdateTask;

public final class ScoreboardModule extends AbstractModule<BingoModuleManager> {

    private ScoreboardUpdateTask updateTask;

    public ScoreboardModule(BingoModuleManager moduleManager) {
        super(moduleManager, "Scoreboard");
    }

    @Override
    protected void onEnable() {
        this.updateTask = new ScoreboardUpdateTask();
        this.updateTask.runTaskTimerAsynchronously(getModuleManager().getPlugin(), 0, 5L);
    }

    @Override
    protected void onDisable() {
        if (updateTask.isCancelled()) return;
        updateTask.cancel();
    }
}
