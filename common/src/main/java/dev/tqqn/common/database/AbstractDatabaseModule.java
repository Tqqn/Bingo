package dev.tqqn.common.database;

import dev.tqqn.common.database.framework.config.DatabaseCredentials;
import dev.tqqn.common.database.framework.objects.BasePlayerModel;
import dev.tqqn.common.database.framework.objects.DefaultConfig;
import dev.tqqn.common.database.framework.objects.MongoDriver;
import dev.tqqn.common.modular.AbstractModule;
import dev.tqqn.common.modular.ModuleManager;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.logging.Level;

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
        boolean doesDatabaseExist;
        if (userName == null) {
            doesDatabaseExist = this.mongoDriver.connect(credentials.database(), credentials.host(), credentials.port());
        } else {
            doesDatabaseExist = this.mongoDriver.connect(credentials.database(), credentials.host(), userName, credentials.password());
        }

        if (!doesDatabaseExist) {
            getLogger().log(Level.SEVERE, "Database does not exist - failed starting module.");
            setShouldDisablePluginOnFail(true);
        }
    }

    public abstract BasePlayerModel getPlayerModel(Player player);
}
