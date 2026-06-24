package dev.tqqn.lobby.modules;

import dev.tqqn.common.modular.ModuleManager;
import dev.tqqn.common.scoreboard.ScoreboardModule;
import dev.tqqn.lobby.LobbyPlugin;
import dev.tqqn.lobby.modules.database.LobbyDatabaseModule;
import dev.tqqn.lobby.modules.lobby.LobbyModule;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 23/06/2026
 */
public class BingoLobbyModuleManager extends ModuleManager<LobbyPlugin> {

    public BingoLobbyModuleManager(LobbyPlugin plugin) {
        super(plugin);

        final LobbyDatabaseModule lobbyDatabaseModule = new LobbyDatabaseModule(this);
        addModule(lobbyDatabaseModule);

        addModule(new LobbyModule(this));
        addModule(new ScoreboardModule(this, lobbyDatabaseModule));
    }
}
