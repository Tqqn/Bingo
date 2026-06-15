package dev.tqqn.common.database.framework.objects;

public interface IDataBaseDriver {

    void connect(String database, String host, int port);
    void connect(String database, String host, String userName, String password);
}
