package dev.tqqn.common.database.framework.objects;

public interface IDataBaseDriver {

    boolean connect(String database, String host, int port);
    boolean connect(String database, String host, String userName, String password);
}
