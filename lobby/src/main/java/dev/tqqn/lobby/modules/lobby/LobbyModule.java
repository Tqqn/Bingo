package dev.tqqn.lobby.modules.lobby;

import dev.tqqn.common.modular.AbstractModule;
import dev.tqqn.lobby.modules.BingoLobbyModuleManager;
import dev.tqqn.lobby.modules.lobby.listeners.LobbyListeners;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 23/06/2026
 */
public final class LobbyModule extends AbstractModule<BingoLobbyModuleManager> {

    public LobbyModule(BingoLobbyModuleManager moduleManager) {
        super(moduleManager, "Lobby");
    }

    @Override
    protected void onEnable() {
        register(new LobbyListeners());
    }
}
