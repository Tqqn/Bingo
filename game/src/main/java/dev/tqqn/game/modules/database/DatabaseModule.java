package dev.tqqn.game.modules.database;

import dev.tqqn.game.BingoMain;
import dev.tqqn.game.modules.AbstractModule;
import dev.tqqn.game.modules.database.framework.config.BingoTaskConfig;
import dev.tqqn.game.modules.database.framework.objects.DefaultConfig;
import dev.tqqn.game.modules.database.framework.objects.MongoDriver;
import dev.tqqn.game.modules.database.listeners.PlayerLoadListener;
import dev.tqqn.game.modules.database.listeners.PlayerQuitListener;
import lombok.Getter;

@Getter
public final class DatabaseModule extends AbstractModule {

    private MongoDriver mongoDriver;
    private final DefaultConfig defaultConfig;
    private BingoTaskConfig bingoTaskConfig;

    public DatabaseModule(BingoMain plugin) {
        super(plugin, "Database");
        this.defaultConfig = DefaultConfig.getInstance(this);
    }

    @Override
    protected void onLoad() {
        this.mongoDriver = new MongoDriver(this);

        final String userName = defaultConfig.getDBUserName();
        if (userName == null) {
            this.mongoDriver.connect(defaultConfig.getDBDataBase(), defaultConfig.getDBHost());
        } else {
            this.mongoDriver.connect(defaultConfig.getDBDataBase(), defaultConfig.getDBHost(), userName, defaultConfig.getDBPassword());
        }

        this.bingoTaskConfig = new BingoTaskConfig(this);
    }

    @Override
    protected void onEnable() {
        register(new PlayerLoadListener(this));
        register(new PlayerQuitListener());
    }
}
