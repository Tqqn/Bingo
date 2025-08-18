package dev.tqqn.modules.database.framework.objects;

public interface IDataBaseDriver {

    void connect(String database, String host, String port);
    void connect(String database, String host, String userName, String password);
}
