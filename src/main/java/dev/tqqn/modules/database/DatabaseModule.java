package dev.tqqn.modules.database;

import dev.tqqn.TemplateMain;
import dev.tqqn.modules.AbstractModule;
import dev.tqqn.modules.database.framework.objects.DefaultConfig;
import dev.tqqn.modules.database.framework.objects.MongoDriver;
import dev.tqqn.modules.database.listeners.PlayerLoadListener;
import lombok.Getter;

@Getter
public final class DatabaseModule extends AbstractModule {

    private MongoDriver mongoDriver;
    private final DefaultConfig defaultConfig;

    public DatabaseModule(TemplateMain plugin) {
        super(plugin, "Database");
        this.defaultConfig = DefaultConfig.getInstance(this);
    }

    @Override
    protected void onLoad() {
        this.mongoDriver = new MongoDriver(this);

        final String userName = defaultConfig.getDBUserName();
        if (userName == null) {
            this.mongoDriver.connect(defaultConfig.getDBDataBase(), defaultConfig.getDBHost(), "27017");
        } else {
            this.mongoDriver.connect(defaultConfig.getDBDataBase(), defaultConfig.getDBHost(), userName, defaultConfig.getDBPassword());
        }

    }

    @Override
    protected void onEnable() {
        register(new PlayerLoadListener(this));
    }
}
