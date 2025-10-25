package dev.tqqn.modules.database;

import dev.tqqn.BingoMain;
import dev.tqqn.modules.AbstractModule;
import dev.tqqn.modules.database.framework.config.BingoTaskConfig;
import dev.tqqn.modules.database.framework.objects.DefaultConfig;
import dev.tqqn.modules.database.framework.objects.MongoDriver;
import dev.tqqn.modules.database.listeners.PlayerLoadListener;
import lombok.Getter;

import java.util.logging.Level;

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
            getPlugin().getLogger().log(Level.SEVERE, "No credentials found for MongoDB. Disabling plugin...");
            getPlugin().getServer().getPluginManager().disablePlugin(getPlugin());
        } else {
            this.mongoDriver.connect(defaultConfig.getDBDataBase(), defaultConfig.getDBHost(), userName, defaultConfig.getDBPassword());
        }

        this.bingoTaskConfig = new BingoTaskConfig(this);
    }

    @Override
    protected void onEnable() {
        register(new PlayerLoadListener(this));
    }
}
