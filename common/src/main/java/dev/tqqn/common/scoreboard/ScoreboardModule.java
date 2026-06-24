package dev.tqqn.common.scoreboard;

import dev.tqqn.common.database.AbstractDatabaseModule;
import dev.tqqn.common.modular.AbstractModule;
import dev.tqqn.common.modular.ModuleManager;
import dev.tqqn.common.scoreboard.task.ScoreboardUpdateTask;
import lombok.Getter;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 24/06/2026
 */
public final class ScoreboardModule extends AbstractModule<ModuleManager<?>> {

    private ScoreboardUpdateTask updateTask;
    @Getter private final AbstractDatabaseModule databaseModule;

    public ScoreboardModule(ModuleManager<?> moduleManager, AbstractDatabaseModule databaseModule) {
        super(moduleManager, "Scoreboard");
        this.databaseModule = databaseModule;
    }

    @Override
    protected void onEnable() {
        this.updateTask = new ScoreboardUpdateTask(databaseModule);
        this.updateTask.runTaskTimerAsynchronously(getModuleManager().getPlugin(), 0L, 5L);
    }

    @Override
    protected void onDisable() {
        if (updateTask == null) return;
        if (updateTask.isCancelled()) return;
        updateTask.cancel();
    }
}
