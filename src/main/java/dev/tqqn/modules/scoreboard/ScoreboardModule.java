package dev.tqqn.modules.scoreboard;

import dev.tqqn.BingoMain;
import dev.tqqn.modules.AbstractModule;
import dev.tqqn.modules.scoreboard.task.ScoreboardUpdateTask;

public final class ScoreboardModule extends AbstractModule {

    private ScoreboardUpdateTask updateTask;

    public ScoreboardModule(BingoMain plugin) {
        super(plugin, "Scoreboard");
    }

    @Override
    protected void onEnable() {
        this.updateTask = new ScoreboardUpdateTask();
        this.updateTask.runTaskTimerAsynchronously(getPlugin(), 0, 5L);
    }

    @Override
    protected void onDisable() {
        if (updateTask.isCancelled()) return;
        updateTask.cancel();
    }
}
