package dev.tqqn.modules.bingo;

import com.google.common.collect.ImmutableList;
import dev.tqqn.BingoMain;
import dev.tqqn.modules.AbstractModule;
import dev.tqqn.modules.bingo.framework.objects.BingoTask;
import dev.tqqn.modules.database.DatabaseModule;

import java.util.ArrayList;
import java.util.List;

public final class BingoModule extends AbstractModule {

    private final DatabaseModule databaseModule;

    private final List<BingoTask> availableTasks = new ArrayList<>();

    public BingoModule(BingoMain plugin, DatabaseModule databaseModule) {
        super(plugin, "Bingo");
        this.databaseModule = databaseModule;
    }

    @Override
    protected void onEnable() {
        availableTasks.addAll(databaseModule.getBingoTaskConfig().getAllTasks());
    }

    public List<BingoTask> getAvailableTasks() {
        return ImmutableList.copyOf(availableTasks);
    }
}
