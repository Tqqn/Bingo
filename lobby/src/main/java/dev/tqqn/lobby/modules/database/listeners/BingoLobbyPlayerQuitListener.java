package dev.tqqn.lobby.modules.database.listeners;

import dev.tqqn.common.database.framework.events.PlayerModelQuitEvent;
import dev.tqqn.common.database.listeners.AbstractPlayerQuitListener;
import dev.tqqn.lobby.modules.database.objects.LobbyPlayerModel;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 12/06/2026
 */
public final class BingoLobbyPlayerQuitListener extends AbstractPlayerQuitListener<LobbyPlayerModel> {

    public BingoLobbyPlayerQuitListener(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    protected LobbyPlayerModel getFromCache(UUID uuid) {
        return LobbyPlayerModel.from(uuid);
    }

    @Override
    protected void removeFromCache(UUID uuid) {
        LobbyPlayerModel.from(uuid).cleanUp();
    }

    @Override
    protected void saveModel(LobbyPlayerModel model) {
        model.save();
    }

    @Override
    protected void onQuit(LobbyPlayerModel model, PlayerQuitEvent event) {
        final PlayerModelQuitEvent quitEvent = new PlayerModelQuitEvent(model);
        Bukkit.getPluginManager().callEvent(quitEvent);
    }
}
