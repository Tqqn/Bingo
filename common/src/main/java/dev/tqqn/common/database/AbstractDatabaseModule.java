package dev.tqqn.common.database;

import dev.tqqn.common.database.framework.config.DatabaseCredentials;
import dev.tqqn.common.database.framework.objects.DefaultConfig;
import dev.tqqn.common.database.framework.objects.MongoDriver;
import dev.tqqn.common.modular.AbstractModule;
import dev.tqqn.common.modular.ModuleManager;
import lombok.Getter;

@Getter
public abstract class AbstractDatabaseModule extends AbstractModule<ModuleManager<?>> {

    private MongoDriver mongoDriver;
    private final DatabaseCredentials credentials;

    public AbstractDatabaseModule(ModuleManager<?> moduleManager) {
        super(moduleManager, "Database");
        this.credentials = DefaultConfig.getInstance(this).getDatabaseCredentials();
    }

    @Override
    protected void onLoad() {
        this.mongoDriver = new MongoDriver(this);

        final String userName = credentials.username();
        if (userName == null) {
            this.mongoDriver.connect(credentials.database(), credentials.host(), credentials.port());
        } else {
            this.mongoDriver.connect(credentials.database(), credentials.host(), userName, credentials.password());
        }
    }
}
