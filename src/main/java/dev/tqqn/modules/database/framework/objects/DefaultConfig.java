package dev.tqqn.modules.database.framework.objects;

import dev.tqqn.TemplateMain;
import dev.tqqn.modules.database.DatabaseModule;

public final class DefaultConfig {

    private static DefaultConfig INSTANCE;

    private final TemplateMain templateMain;

    private DefaultConfig(DatabaseModule databaseModule) {
        this.templateMain = databaseModule.getPlugin();
        templateMain.saveDefaultConfig();
    }

    public static DefaultConfig getInstance(DatabaseModule databaseModule) {
        if (INSTANCE == null) {
            INSTANCE = new DefaultConfig(databaseModule);
        }
        return INSTANCE;
    }

    public String getDBHost() {
        return templateMain.getConfig().getString("database.host");
    }

    public String getDBDataBase() {
        return templateMain.getConfig().getString("database.database");
    }

    public String getDBUserName() {
        return templateMain.getConfig().getString("database.username");
    }

    public String getDBPassword() {
        return templateMain.getConfig().getString("database.password");
    }
}
