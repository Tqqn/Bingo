package dev.tqqn.common.database.framework.config;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 11/06/2026
 */

public record DatabaseCredentials(String host, int port, String database, String username, String password) {
}
