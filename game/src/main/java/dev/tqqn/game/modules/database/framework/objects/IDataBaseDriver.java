package dev.tqqn.game.modules.database.framework.objects;

public interface IDataBaseDriver {

    void connect(String database, String host);
    void connect(String database, String host, String userName, String password);
}
