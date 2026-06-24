package dev.tqqn.lobby.modules.database;

import dev.tqqn.common.database.AbstractDatabaseModule;
import dev.tqqn.common.modular.ModuleManager;
import dev.tqqn.lobby.modules.database.listeners.BingoLobbyPlayerLoadListener;
import dev.tqqn.lobby.modules.database.listeners.BingoLobbyPlayerQuitListener;
import dev.tqqn.lobby.modules.database.objects.LobbyPlayerModel;
import org.bukkit.entity.Player;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 24/06/2026
 */
public final class LobbyDatabaseModule extends AbstractDatabaseModule {

    public LobbyDatabaseModule(ModuleManager<?> moduleManager) {
        super(moduleManager);
    }

    @Override
    protected void onEnable() {
        register(new BingoLobbyPlayerLoadListener(this, getMongoDriver()));
        register(new BingoLobbyPlayerQuitListener(getModuleManager().getPlugin()));
    }

    @Override
    public LobbyPlayerModel getPlayerModel(Player player) {
        return LobbyPlayerModel.from(player);
    }
}
